package org.claim.audit.common.activiti.listener;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;




import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Gateway;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.claim.audit.common.activiti.ActivitiConstants;
import org.claim.audit.common.util.JsonUtil;
import org.claim.audit.common.vo.TaskInfoVo;
import org.springframework.stereotype.Component;

@Component
public class TaskCreatedListener implements ActivitiEventListener {
	
	private RepositoryService repositoryService;
    public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	public void onEvent(ActivitiEvent event){
    	
        TaskEntity taskEntity = (TaskEntity)((ActivitiEntityEvent)event).getEntity();
        Integer mark = (Integer)taskEntity.getVariable(ActivitiConstants.MARKTARGETTASKID);  
      
        if(mark==null) {
        	return;
        }else{
        	   Process process = repositoryService.getBpmnModel(
               		taskEntity.getProcessDefinitionId()).getMainProcess();          
               FlowNode fe = (FlowNode)process.getFlowElement(taskEntity.getTaskDefinitionKey());
               SequenceFlow sf =  (SequenceFlow)fe.getIncomingFlows().get(0);
               FlowElement obj = sf.getSourceFlowElement();
        	  if(obj instanceof Gateway && !(obj instanceof ExclusiveGateway) ){
	              throw new ActivitiException("出现并行网关，无法跳转！");
	              //子流程等特殊场景都可以同样在此过滤，不允许跳转
              }
        	String json= (String)taskEntity.getVariable(ActivitiConstants.TRACKHISTASKINFO);
        	LinkedHashMap<String,TaskInfoVo> linkList = JsonUtil.JsonToObject(json, LinkedHashMap.class,String.class,TaskInfoVo.class);
        	String taskDefinitionKey = taskEntity.getTaskDefinitionKey();
        	if(ActivitiConstants.STARTTASKNODE.equals(mark)){
        		taskEntity.setAssignee(linkList.get(taskDefinitionKey).getAssignee());
            	taskEntity.setOwner(linkList.get(taskDefinitionKey).getOwner());
            	//打回到发起人，则需要清空轨迹任务数据
            	taskEntity.setVariable(ActivitiConstants.TRACKHISTASKINFO, null);
        	}else if(ActivitiConstants.BEFORETASKNODE.equals(mark)){
        		//给跳转后的任务赋值原来的委派人和拥有人，保证任务退回到当时的执行人手里
            	taskEntity.setAssignee(linkList.get(taskDefinitionKey).getAssignee());
            	taskEntity.setOwner(linkList.get(taskDefinitionKey).getOwner());
            	linkList.remove(taskDefinitionKey);
            	taskEntity.setVariable(ActivitiConstants.TRACKHISTASKINFO, JsonUtil.ObjectToJson(linkList));
        	}else if(ActivitiConstants.ENDTASKNODE.equals(mark)){
        		
        	}else if(ActivitiConstants.ANYTASKNODE.equals(mark)){

        		String tdk = taskEntity.getTaskDefinitionKey();
        		boolean deleteFlag =false;
        		Set<String> set = linkList.keySet();
        		
        		Iterator<String> iterator= set.iterator();
        		while(iterator.hasNext()){
        			String key = iterator.next();
        			if(deleteFlag){
        				iterator.remove();
        				continue;
        			}
        			if(tdk.equals(key)){
        				//跳转到任务轨迹中的其中一个任务
        				taskEntity.setAssignee(linkList.get(key).getAssignee());
                    	taskEntity.setOwner(linkList.get(key).getOwner());
                    	deleteFlag=true;
                    	iterator.remove();
        			}
        		}
        		if(!deleteFlag){
        			throw new ActivitiException("无法跳转到"+taskEntity.getProcessInstance().getProcessDefinitionKey()+"流程中的"+tdk+"任务！");
        		}else{
        			taskEntity.setVariable(ActivitiConstants.TRACKHISTASKINFO, JsonUtil.ObjectToJson(linkList));
        		}
        	}
        	
        	//清空mark
        	taskEntity.setVariable(ActivitiConstants.MARKTARGETTASKID,null);
        }
        //可以通过持久化方式入库，但是由于该数据没有意义，因此只通过流程变量处理，减少冗余数据
    }
    public boolean isFailOnException(){
        return true;
    }
}