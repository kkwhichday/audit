package org.claim.audit.common.activiti.listener;

import java.util.LinkedHashMap;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.claim.audit.common.activiti.ActivitiConstants;
import org.claim.audit.common.util.JsonUtil;
import org.claim.audit.common.vo.TaskInfoVo;
import org.springframework.stereotype.Component;

@Component
public class TaskCompletedListener implements ActivitiEventListener {
	public void onEvent(ActivitiEvent event) {

		TaskEntity taskEntity = (TaskEntity) ((ActivitiEntityEvent) event)
				.getEntity();
		String str = (String) taskEntity
				.getVariable(ActivitiConstants.TRACKHISTASKINFO);
	
		LinkedHashMap<String,TaskInfoVo> linkList = null;
		if (str == null ||str.length()==0) {
		
			linkList = new LinkedHashMap<String,TaskInfoVo>();
		} else {
			linkList = JsonUtil
					.JsonToObject(str, LinkedHashMap.class,String.class,TaskInfoVo.class);
		}
		TaskInfoVo tif = new TaskInfoVo(taskEntity.getId(),
				taskEntity.getAssignee(), taskEntity.getOwner());
		taskEntity.getTaskDefinitionKey();
		linkList.put(taskEntity.getTaskDefinitionKey(), tif);
		String json = JsonUtil.ObjectToJson(linkList);
		taskEntity.setVariable(ActivitiConstants.TRACKHISTASKINFO, json);
	}

	public boolean isFailOnException() {
		return true;
	}
}