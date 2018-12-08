package org.claim.audit.vacation.vo;


import java.util.Date;

import lombok.Data;


/**
 * @author pc
 *存储业务信息
 */
@Data
public class Vacation {

    /**
     * 申请人
     */
    private String applyUser;
    private int days;
    private String reason;
    private Date applyTime;
    private String applyStatus;

    /**
     * 审核人
     */
    private String auditor;
    private String result;
    private Date auditTime;
    
	@Override
	public String toString() {
		return "Vacation [applyUser=" + applyUser + ", days=" + days
				+ ", reason=" + reason + ", applyTime=" + applyTime
				+ ", applyStatus=" + applyStatus + ", auditor=" + auditor
				+ ", result=" + result + ", auditTime=" + auditTime + "]";
	}

}
