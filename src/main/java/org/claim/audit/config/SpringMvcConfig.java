package org.claim.audit.config;

import org.claim.audit.common.web.CorsCrossInterceptor;
import org.claim.audit.common.web.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootConfiguration
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private TokenInterceptor tokenInterceptor;
	@Autowired
	private CorsCrossInterceptor corsCrossInterceptor;
	
	public void addInterceptors(InterceptorRegistry interceptorregistry)
    {
		interceptorregistry.addInterceptor(corsCrossInterceptor).addPathPatterns("/**");
		interceptorregistry.addInterceptor(tokenInterceptor).addPathPatterns("/**");
    }
	
}
