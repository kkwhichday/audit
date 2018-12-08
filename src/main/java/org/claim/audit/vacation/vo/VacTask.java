package org.claim.audit.vacation.vo;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class VacTask {

    private String id;
    private String name;
    private Vacation vac;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	@Override
	public String toString() {
		return "VacTask [id=" + id + ", name=" + name + ", vac=" + vac
				+ ", createTime=" + createTime + "]";
	}

   
}
