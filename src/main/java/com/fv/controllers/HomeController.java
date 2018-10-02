package com.fv.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

@Controller
public class HomeController {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@RequestMapping(value = "/home")
	public String home(HttpServletRequest request) {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (principal == "anonymousUser") { // Logado pelo IDP
			
			try {
				
				UserProvider provider = UserManagementAccessor.getUserProvider();
				User user = provider.getCurrentUser();
				
//				replicateUsuarioService.replicateUserRoles(user);
				
				UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getAttribute("username"), "inicial");

			    // Authenticate the user
			    Authentication authentication = authenticationManager.authenticate(authRequest);
			    SecurityContextHolder.getContext().setAuthentication(authentication);

			    // Create a new session and add the security context.
			    HttpSession session = request.getSession(true);
			    session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

			    
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return "home";
	}
}
