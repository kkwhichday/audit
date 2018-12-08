package org.claim.audit.vacation.controller;


import java.util.List;

import org.claim.audit.common.util.ThreadLocalUtil;
import org.claim.audit.common.vo.PageBean;
import org.claim.audit.vacation.service.facade.VacationService;
import org.claim.audit.vacation.vo.VacTask;
import org.claim.audit.vacation.vo.Vacation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class VacationController {

    @Resource
    private VacationService vacationService;

    @PostMapping("/startVac")
    public Object startVac(@RequestBody Vacation vac) {
        String userName = ThreadLocalUtil.getUser().getName();
        return vacationService.startVac(userName, vac);
    }
    //用spring的service分配任务
    @PostMapping("/startVac2")
    public Object startVac2(@RequestBody Vacation vac) {
        String userName = ThreadLocalUtil.getUser().getName();
        return vacationService.startVac2(userName, vac);
    }

    //查询当前用户的病假条明细
    @GetMapping("/getCurrentVac")
    public Object myVac() {
        String userName = ThreadLocalUtil.getUser().getName();
        return vacationService.myVac(userName);
    }

    
    //查询当前用户的带办任务
	@GetMapping("/currentTaskVac")
    public Object myAudit( int pageSize) {
        String userName = ThreadLocalUtil.getUser().getName();
        List<VacTask> vacList= vacationService.myAuditCustom(userName);
        return new PageBean<VacTask>(vacList,pageSize);
    }

    
    //完成审核任务，审核通过
    @PostMapping("/passAudit")
    public Object passAudit(@RequestBody VacTask vacTask) {
        String userName = ThreadLocalUtil.getUser().getName();
        return vacationService.passAudit(userName, vacTask);
    }

    //查询历史病假记录
    @GetMapping("/getHistoryVacRecord")
    public Object myVacRecord() {
        String userName = ThreadLocalUtil.getUser().getName();
        return vacationService.myVacRecord(userName);
    }

    //查询历史的审核记录
    @GetMapping("/getHistoryAuditRecord")
    public Object myAuditRecord() {
        String userName = ThreadLocalUtil.getUser().getName();
        return vacationService.myAuditRecord(userName);
    }

}
