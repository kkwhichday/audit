package org.claim.audit.vacation.service.facade;

import java.util.List;

import org.claim.audit.vacation.vo.VacTask;
import org.claim.audit.vacation.vo.Vacation;

public interface VacationService {
	 public Object startVac(String userName, Vacation vac);
	 public Object startVac2(String userName, Vacation vac);
	 public Object myVac(String userName) ;
	 /**
	 * @param userName
	 * @return
	 * 根据activity自带的用户、组功能查询代办任务
	 */
	public Object myAudit(String userName);
	
	/**
	 * @param userName
	 * @return
	 * 根据系统自定义的用户表和角色表查询代办任务
	 */
	public  List<VacTask> myAuditCustom(String userName);
	 public Object passAudit(String userName, VacTask vacTask);
	 public Object myVacRecord(String userName);
	 public Object myAuditRecord(String userName);

}
