package org.claim.audit.common.controller;


import org.claim.audit.common.util.ThreadLocalUtil;
import org.claim.audit.common.vo.UserBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	@GetMapping(value = "/getinfo")
	public UserBean getUserInfo() {

		return ThreadLocalUtil.getUser();
	}
	@PostMapping("/login")
    public void login() {
       
       
    }



}
