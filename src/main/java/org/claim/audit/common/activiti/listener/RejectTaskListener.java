package org.claim.audit.common.activiti.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.claim.audit.common.activiti.ActivitiConstants;

/**
 * @author pc
 *	需要静态驳回和动态驳回混用时，必须给静态驳回挂上这个监听器
 */
public class RejectTaskListener implements ExecutionListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) {
		// 为跳转mark一下
		execution.setVariable(ActivitiConstants.MARKTARGETTASKID ,ActivitiConstants.ANYTASKNODE);		
	
	}

}
