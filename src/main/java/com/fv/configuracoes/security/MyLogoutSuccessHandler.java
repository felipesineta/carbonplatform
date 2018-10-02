package com.fv.configuracoes.security;

import java.io.IOException;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.sap.security.auth.login.LoginContextFactory;

@Component("myLogoutSuccessHandler")
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		final HttpSession session = request.getSession();
        if (session != null) {
            session.removeAttribute("SPRING_SECURITY_CONTEXT");
        }
        LoginContext loginContext = null;
  	  
    	try { 
    		
	      loginContext = LoginContextFactory.createLoginContext(); 
	      loginContext.logout();
	     
	    } catch (LoginException e) { 
	      // Servlet container handles the login exception
	      // It throws it to the application for its information
	    	e.printStackTrace();
	    }
        
        response.sendRedirect(request.getContextPath());
	}

}
