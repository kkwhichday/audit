package org.claim.audit.common.activiti;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.claim.audit.common.activiti.listener.TaskCompletedListener;
import org.claim.audit.common.activiti.listener.TaskCreatedListener;
import org.springframework.stereotype.Component;

@Component
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer {

	@Resource
	TaskCreatedListener taskCreatedListener;
	@Resource
	TaskCompletedListener taskCompletedListener;
	
   public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
	   taskCreatedListener.setRepositoryService(processEngineConfiguration.getRepositoryService());
		Map<String, List<ActivitiEventListener>> typedListeners = new HashMap<>();
        typedListeners.put("TASK_CREATED", Collections.singletonList(taskCreatedListener));
        typedListeners.put("TASK_COMPLETED", Collections.singletonList(taskCompletedListener));

        processEngineConfiguration.setTypedEventListeners(typedListeners);

   }

}