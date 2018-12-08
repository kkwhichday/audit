package org.claim.audit.common.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfoVo {
	private String taskid;
	private String assignee;
	private String owner;	
}
