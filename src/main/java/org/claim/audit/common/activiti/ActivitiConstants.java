package org.claim.audit.common.activiti;

public class ActivitiConstants {

	public static final String TRACKHISTASKINFO= "TRACK_HIS_TASK_INFO";
	public static final String MARKTARGETTASKID="MARK_TARGET_TASK_ID";
	public static final Integer STARTTASKNODE=0; //起始任务
	public static final Integer BEFORETASKNODE=1; //上一级的任务节点，不包含分支、多实例等
	public static final Integer ENDTASKNODE=2;//终止任务
	public static final Integer ANYTASKNODE=3;//任意跳转任务
	
}
