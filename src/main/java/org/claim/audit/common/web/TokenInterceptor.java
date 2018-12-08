package org.claim.audit.common.web;

import org.claim.audit.common.exception.BusinessException;
import org.claim.audit.common.service.facade.UserService;
import org.claim.audit.common.util.JwtUtil;
import org.claim.audit.common.util.ThreadLocalUtil;
import org.claim.audit.common.vo.LoginBean;
import org.claim.audit.common.vo.ResponseData;
import org.claim.audit.common.vo.UserBean;
import org.claim.audit.common.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserService userService;
    
  //拦截每个请求
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("Claim-Token");
        ResponseData responseData = ResponseData.ok();
       
        UserBean user = null;
        log.info("token==="+token);;
        //token存在
        if(!"false".equals(token)&&!"undefined".equals(token)) {
        	try{
        		user = jwtUtil.unsign(token, UserBean.class);
        		//保存用户信息，拦截器在进入过滤器之后才调用，因此该用户信息对过滤器不可见，只能被controller，service访问
        		ThreadLocalUtil.setUser(user);
//        		responseMessage(response, response.getWriter(), responseData);
        		return true;
        	}catch(BusinessException e){
        		responseData = e.getResMsg();
        		responseMessage(response,response.getWriter(),responseData);
                return false;
        	}
        }else{
        	//token为空且访问登录地址，则认为是首次登录
        	if(request.getRequestURI().contains("/login")){
        		ObjectMapper objectMapper = new ObjectMapper();
        		String username,password;        		
        		LoginBean loginBean = objectMapper.readValue(request.getInputStream(),LoginBean.class);
        		if(loginBean!=null && 
        			!StringUtils.isEmpty(username=loginBean.getUserName())&&
        				!StringUtils.isEmpty(password=loginBean.getPassWord())){       		
        			User realuser = userService.findUserByName(username);
	        	
	        		if(realuser!=null && realuser.getPassword().equals(password)){
	        			UserBean payload = new UserBean();
	        			payload.setUser_id(realuser.getId());
	        			payload.setName(realuser.getUserName());
	        			payload.setRoles(Arrays.asList("Admin","staff"));//这里应该从库里取
	        			token = jwtUtil.sign(payload);
	        			responseData.putDataValue("token", token);
	        			//生成token成功，放进response
	        			responseMessage(response, response.getWriter(), responseData);
	                    return true;
	        		}
	        	}
        	}
            responseData = ResponseData.unauthorized();
            responseMessage(response, response.getWriter(), responseData);
            return false;
        }
    }

    //返回响应信息给客户端
    private void responseMessage(HttpServletResponse response, PrintWriter out, ResponseData responseData) {
    	if(responseData.getCode()!=200){
    		responseData = ResponseData.forbidden();
    	}
        response.setContentType("application/json; charset=utf-8"); 
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
		try {
			json = objectMapper.writeValueAsString(responseData);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json="{code:'JsonError',message:'解析json异常'},data:''";
		}
        out.print(json);
        out.flush();
        out.close();
    }

	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		//结束请求后删除用户信息,同样由于包裹在过滤器之内，因此过滤器看不到该用户信息
		ThreadLocalUtil.removeUser();
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}



}