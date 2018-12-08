package org.claim.audit;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.claim.audit.common.model.User;
import org.claim.audit.common.service.facade.UserService;
import org.claim.audit.vacation.service.facade.VacationService;
import org.claim.audit.vacation.vo.VacTask;
import org.claim.audit.vacation.vo.Vacation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest{

    @Resource
    private ProcessEngineConfiguration configuration;
    @Resource
    private ProcessEngine engine;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private IdentityService identityService;
    
    @Resource
    private UserService userService;
    
    @Resource
    private VacationService vacationService;

//	@Test
    public void test() {
//        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService is = engine.getIdentityService();
        // 添加用户组
        Group empGroup = saveGroup(is, "empGroup", "员工组");
        Group manageGroup = saveGroup(is, "manageGroup", "经理组");
        Group dirGroup = saveGroup(is, "dirGroup", "总监组");
        // 添加用户
        org.activiti.engine.identity.User empA = saveUser(is, "empa"); // 员工a
        org.activiti.engine.identity.User  empB = saveUser(is, "empb"); // 员工b
        org.activiti.engine.identity.User  managea = saveUser(is, "managea"); // 经理a
        org.activiti.engine.identity.User  manageb = saveUser(is, "manageb"); // 经理b
        org.activiti.engine.identity.User  dira = saveUser(is, "dira"); // 总监a
        // 绑定关系
        saveRel(is, empA, empGroup);
        saveRel(is, empB, empGroup);
        saveRel(is, managea, manageGroup);
        saveRel(is, manageb, manageGroup);
        saveRel(is, dira, dirGroup);
    }

    org.activiti.engine.identity.User  saveUser(IdentityService is, String id) {
    	 org.activiti.engine.identity.User  u = is.newUser(id);
        u.setPassword("123456");
        is.saveUser(u);
        return u;
    }

    Group saveGroup(IdentityService is, String id, String name) {
        Group g = is.newGroup(id);
        g.setName(name);
        is.saveGroup(g);
        return g;
    }

    void saveRel(IdentityService is,  org.activiti.engine.identity.User  u, Group g) {
        is.createMembership(u.getId(), g.getId());
    }


//    @Test
    @Transactional
    @Rollback(false)
    public void test2() {
//	    / 无用 ： spring自动部署流程文件
//        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        // 存储服务
        RepositoryService rs = engine.getRepositoryService();
//        Deployment dep = rs.createDeployment().addClasspathResource("processes/vacation.bpmn").deploy();
        ProcessDefinition pd = rs.createProcessDefinitionQuery().deploymentId("5001").singleResult();
        rs.addCandidateStarterGroup(pd.getId(), "empGroup");
    }
    
//    @Test
    public void test3() {
    	  Map<String, Object> startvars = new HashMap<>();
          startvars.put("emp", "有效");
        // 开始流程
        ProcessInstance vacationInstance = runtimeService.startProcessInstanceByKey("vacation2Process",startvars);
        // 查询当前任务
        Task currentTask = taskService.createTaskQuery().processInstanceId(vacationInstance.getId()).singleResult();
      
        
        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser("empa1")
                .orderByTaskCreateTime().desc().list();
        
        List<Task> taskList2 = taskService.createTaskQuery().taskCandidateUser("empa")
                .orderByTaskCreateTime().desc().list();
        
        // 申明任务
        taskService.claim(currentTask.getId(), "empa");
        
        
    }
    
//    @Test
    public void test4(){
//    	String groups = userService.findGroupEmployee("有效");
//    	System.out.println(groups);
    	Vacation vac = new Vacation();
    	vac.setApplyTime(new Date());
    	vac.setApplyUser("empa");
    	vac.setReason("感冒");
    	vac.setDays(5);
    	
    	vacationService.startVac("sss", vac);
    }
    @Test
    public void test5(){
    	/*vacationService.myAuditCustom("dirc");
    	 List<VacTask> vts= vacationService.myAuditCustom("dira");
    	 System.out.println(vts);*/
    	User user = userService.findUserByName("managerb");
    	if(user==null){return;}
    	String groupId = user.getGroupId();
	    List<Task> list = taskService.createTaskQuery().taskCandidateGroup(groupId).list();
    }
    
    
}

