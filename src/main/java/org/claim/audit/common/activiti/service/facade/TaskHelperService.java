package org.claim.audit.common.activiti.service.facade;

import java.util.List;

import org.activiti.engine.history.HistoricVariableInstance;


public interface TaskHelperService {
	
    /**
     * 将历史参数列表设置到实体中去
     * @param entity 实体
     * @param varInstanceList 历史参数列表
     */
	public  <T> void setVars(T entity, List<HistoricVariableInstance> varInstanceList) ;
	
    /**跳转方法
     * @param taskId   当前任务id
     * @param targetTask 目标任务节点
     * @param flag 
     * 0:打回到流程发起人  
     * 1:打回到上一级任务节点 
     * 2:跳到任务终止节点， 一般用于特批流程，直接跳过审核
     * 3:跳到任意任务节点， 一般用于动态跳转任务
     * @param taskDefinitionKey flag选2或3时，需要设置具体任务节点的id，保证能跳转到该节点
     * 
     */
	public void jump(String taskId,  Integer flag,String taskDefinitionKey );
}
