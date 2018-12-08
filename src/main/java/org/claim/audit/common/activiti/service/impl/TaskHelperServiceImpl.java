package org.claim.audit.common.activiti.service.impl;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.cmd.NeedsActiveTaskCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManagerImpl;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.claim.audit.common.activiti.ActivitiConstants;
import org.claim.audit.common.activiti.service.facade.TaskHelperService;
import org.claim.audit.common.util.JsonUtil;
import org.claim.audit.common.vo.TaskInfoVo;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskHelperServiceImpl implements TaskHelperService {
	@Resource
	private RuntimeService runtimeService;
	@Resource
	private RepositoryService repositoryService;
	@Resource
	private TaskService taskService;
	@Resource
	private ManagementService managementService;

	@Resource
	private HistoryService historyService;

	public <T> void setVars(T entity,
			List<HistoricVariableInstance> varInstanceList) {
		Class<?> tClass = entity.getClass();
		try {
			for (HistoricVariableInstance varInstance : varInstanceList) {
				Field field = tClass.getDeclaredField(varInstance
						.getVariableName());
				if (field == null) {
					continue;
				}
				field.setAccessible(true);
				field.set(entity, varInstance.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void jump(String taskId, Integer flag, String taskDefinitionKey) {
		// 当前任务
		Task currentTask = taskService.createTaskQuery().taskId(taskId)
				.singleResult();
		// 获取流程定义
		Process process = repositoryService.getBpmnModel(
				currentTask.getProcessDefinitionId()).getMainProcess();
		FlowNode targetNode = null;
		if (ActivitiConstants.STARTTASKNODE.equals(flag)) {
			targetNode = (FlowNode) process.getInitialFlowElement();
		} else if (ActivitiConstants.BEFORETASKNODE.equals(flag)) {
			String json = (String) runtimeService.getVariable(
					currentTask.getExecutionId(),
					ActivitiConstants.TRACKHISTASKINFO);
			LinkedHashMap<String, TaskInfoVo> linklist = JsonUtil.JsonToObject(
					json, LinkedHashMap.class, String.class, TaskInfoVo.class);
			Iterator<Entry<String, TaskInfoVo>> iterator = linklist.entrySet()
					.iterator();
			Entry<String, TaskInfoVo> tail = null;
			while (iterator.hasNext()) {
				tail = iterator.next();
			}
			TaskInfo targetTask = (TaskInfo) historyService
					.createHistoricTaskInstanceQuery()
					.taskId(tail.getValue().getTaskid()).finished()
					.singleResult();
			// 获取目标节点定义
			targetNode = (FlowNode) process.getFlowElement(targetTask
					.getTaskDefinitionKey());
		} else if (ActivitiConstants.ENDTASKNODE.equals(flag)) {
			targetNode = (FlowNode) process.getFlowElement(taskDefinitionKey);
		} else if (ActivitiConstants.ANYTASKNODE.equals(flag)) {
			targetNode = (FlowNode) process.getFlowElement(taskDefinitionKey);
		}
		if (targetNode == null) {
			throw new ActivitiException("跳转节点,flag=" + flag + " ,无法获取"
					+ process.getName() + "的目标顺序流节点" + taskDefinitionKey);
		}
		// 删除当前运行任务
		String executionEntityId = managementService
				.executeCommand(new DeleteTaskCmd(currentTask.getId(), flag));
		// 流程执行到来源节点
		managementService.executeCommand(new SetFLowNodeAndGoCmd(targetNode,
				executionEntityId, flag));
	}

	private static class DeleteTaskCmd extends NeedsActiveTaskCmd<String> {

		private static final long serialVersionUID = 1L;
		private Integer flag;

		public DeleteTaskCmd(String taskId, Integer flag) {
			super(taskId);
			this.flag = flag;
		}

		public String execute(CommandContext commandContext,
				TaskEntity currentTask) {
			// 获取所需服务
			TaskEntityManagerImpl taskEntityManager = (TaskEntityManagerImpl) commandContext
					.getTaskEntityManager();
			// 获取当前任务的来源任务及来源节点信息
			ExecutionEntity executionEntity = currentTask.getExecution();

			log.info("任务" + currentTask.getId() + "发生跳转,正在删除当前任务");
			String str = null;
			if (ActivitiConstants.STARTTASKNODE.equals(flag)) {
				str = "任务回退到发起人";
			} else if (ActivitiConstants.BEFORETASKNODE.equals(flag)) {
				str = "任务回退到上一次任务";
			}
			// 删除当前任务,来源任务
			taskEntityManager.deleteTask(currentTask, str, false, false);
			return executionEntity.getId();
		}

		public String getSuspendedTaskException() {
			return "挂起的任务不能跳转";
		}
	}

	// 根据提供节点和执行对象id，进行跳转命令
	private class SetFLowNodeAndGoCmd implements Command<Void> {
		private Integer flag;
		private FlowNode flowElement;
		private String executionId;

		public SetFLowNodeAndGoCmd(FlowNode flowElement, String executionId,
				Integer flag) {
			this.flowElement = flowElement;
			this.executionId = executionId;
			this.flag = flag;
		}

		public Void execute(CommandContext commandContext) {
			// 获取目标节点的来源连线
			List<SequenceFlow> flows = null;
			if (ActivitiConstants.STARTTASKNODE.equals(flag)) {
				flows = flowElement.getOutgoingFlows();
				log.info("当前任务跳转到发起人");
			} else if (ActivitiConstants.BEFORETASKNODE.equals(flag)) {
				flows = flowElement.getIncomingFlows();
				log.info("任务回退到上一级任务节点");
			} else if (ActivitiConstants.ENDTASKNODE.equals(flag)) {
				flows = flowElement.getIncomingFlows();
				log.info("当前跳到终止任务节点");
			} else if (ActivitiConstants.ANYTASKNODE.equals(flag)) {
				flows = flowElement.getIncomingFlows();
				log.info("当前跳到任意任务节点");
			}
			if (flows == null || flows.size() < 1) {
				throw new ActivitiException("回退错误，目标节点没有来源连线");
			}
			// 随便选一条连线来执行，时当前执行计划为，从连线流转到目标节点，实现跳转
			ExecutionEntity executionEntity = commandContext
					.getExecutionEntityManager().findById(executionId);
			// 为跳转mark一下
			executionEntity.setVariable(ActivitiConstants.MARKTARGETTASKID,
					flag);
			executionEntity.setCurrentFlowElement(flows.get(0));

			commandContext.getAgenda().planTakeOutgoingSequenceFlowsOperation(
					executionEntity, true);
			return null;
		}
	}
}
