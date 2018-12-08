package org.claim.audit;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.claim.audit.common.activiti.ActivitiConstants;
import org.claim.audit.common.activiti.service.facade.TaskHelperService;
import org.claim.audit.common.model.User;
import org.claim.audit.common.service.facade.UserService;
import org.claim.audit.common.util.JsonUtil;
import org.claim.audit.common.vo.TaskInfoVo;
import org.claim.audit.vacation.service.facade.VacationService;
import org.claim.audit.vacation.vo.VacTask;
import org.claim.audit.vacation.vo.Vacation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest2{


    @Resource
    private ProcessEngine engine;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;

    
    @Resource
    private UserService userService;
    
    @Resource
    private VacationService vacationService;

    @Resource
    private RepositoryService repositoryService;
    
    @Resource
    private HistoryService historyService;
    @Resource
    private TaskHelperService taskHelperService;

//   @Test
//   @Transactional
//   @Rollback(false)
    public void test3() {
  	  Map<String, Object> startvars = new HashMap<>();
//        startvars.put("emp", "有效");
		Vacation vac = new Vacation();
		vac.setApplyUser("xxx");
		vac.setDays(5);
		vac.setReason("感冒了");

      vacationService.startVac("empb", vac);
	

  }
    @Test
    @Transactional
    @Rollback(false)
    public  void myAuditPass(){
   
    	String taskId = null;
//    	List<VacTask> list =vacationService.myAuditCustom("empb");
//    	for(VacTask vac:list){
//    		taskId = vac.getId();
//        	taskService.claim(taskId, "empb");
//        	 Map<String, Object> vars = new HashMap<>(4);
//             vars.put("applyUser", "empb");
//             vars.put("days", 2);
//             vars.put("reason","又感冒了啊");
//        	taskService.complete(taskId,vars);
//    	}
    	
    	Task currentTask1 = taskService.createTaskQuery().taskAssignee("managera").singleResult();
    	taskService.complete(currentTask1.getId());
     	Task currentTask2 = taskService.createTaskQuery().taskAssignee("managerb").singleResult();
    	taskService.complete(currentTask2.getId());
     	Task currentTask3 = taskService.createTaskQuery().taskAssignee("managerc").singleResult();
    	taskService.complete(currentTask3.getId());
//    	Task currentTask1 = taskService.createTaskQuery().taskAssignee("empb").singleResult();
//    	taskService.complete(currentTask1.getId());
//    	List<VacTask> list2 =vacationService.myAuditCustom("dira");
//    	for(VacTask vac:list2){
//    		taskId = vac.getId();
//        	taskService.claim(taskId, "dira");
//        	Task currentTask1 = taskService.createTaskQuery().taskId(taskId).singleResult();
//        	ExecutionQuery excecutionQuery =runtimeService.createExecutionQuery();
//        	System.out.println(currentTask1.getProcessInstanceId());
//        	
//        	//查出当前实例下的消息事件相关的执行器
//        	Execution excecution =excecutionQuery.processInstanceId(currentTask1.getProcessInstanceId()).messageEventSubscriptionName("子流程回退").singleResult();
//          
//        	runtimeService.messageEventReceived("子流程回退", excecution.getId());
//    	}
    	
//    	Task currentTask1 = taskService.createTaskQuery().taskAssignee("empb").singleResult();
//    	taskService.complete(currentTask1.getId());
//    	Task currentTask2 = taskService.createTaskQuery().taskAssignee("empa").singleResult();
//    	taskService.complete(currentTask2.getId());
//    	
//    	List<VacTask> list2 =vacationService.myAuditCustom("dira");
//
//    	System.out.println(list2);
//    	for(VacTask vac:list2){
//    		taskId = vac.getId();
//        	taskService.claim(taskId, "dira");
//        	taskService.complete(taskId);
//    	}
//    	List<VacTask> list3 =vacationService.myAuditCustom("bossa");
//
//    	System.out.println(list3);
//    	for(VacTask vac:list3){
//    		taskId = vac.getId();
//        	taskService.claim(taskId, "bossa");
//        	taskService.complete(taskId);
//    	}
//
    } 
//    
//    @Test
//    @Transactional
//    @Rollback(false)
    public  void myAuditCustom(){
    	String taskId = null;
//    	Task currentTask1 = taskService.createTaskQuery().taskAssignee("dira").singleResult();
//    	taskId = currentTask1.getId();
    	
//    	List<VacTask> list2 =vacationService.myAuditCustom("dira");
//
//    	System.out.println(list2);
//    	for(VacTask vac:list2){
//    		taskId = vac.getId();
//    		taskService.claim(taskId, "dira");
//        	Map<String, Object> vars = new HashMap<>();
//            vars.put("result", "yes");
//        	taskService.complete(taskId,vars);
//    	}

    	
    	
//    	List<VacTask> list =vacationService.myAuditCustom("bossa");
//    	taskId = list.get(0).getId();
//    	taskService.claim(taskId, "bossa");
//    	taskService.complete(taskId);
    	
    	 //当前任务
       	List<VacTask> list =vacationService.myAuditCustom("bossa");
    	taskId = list.get(0).getId();
    	taskService.claim(taskId, "bossa");
//    	Task currentTask2 = taskService.createTaskQuery().taskAssignee("dira").singleResult();
//    	taskId = currentTask2.getId();
//    	Map<String, Object> vars = new HashMap<>();
//        vars.put("result", "yes");

//    	taskService.complete(taskId,vars);
    	taskHelperService.jump(taskId, ActivitiConstants.STARTTASKNODE,"usertask2");

    	 
   
    	
    }
    

    
}

