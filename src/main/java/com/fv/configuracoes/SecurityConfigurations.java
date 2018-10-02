package com.fv.configuracoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fv.configuracoes.security.MyLogoutSuccessHandler;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
    PasswordEncoder passwordEncoder;

	@Autowired
	MyLogoutSuccessHandler myLogoutSuccessHandler;
	
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	   @Override
	   public AuthenticationManager authenticationManagerBean() throws Exception {
	       return super.authenticationManagerBean();
	   }
	
	@Bean
	public UserDetailsService userDetailsService() {
	    return super.userDetailsService();
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
		.csrf().disable()
		.authorizeRequests()
			.antMatchers("/app icon v2.png").permitAll()
			.antMatchers("/cadastro").permitAll()
			.antMatchers("/api/cadastro/novo").permitAll()
			.anyRequest().authenticated()
		.and()
        	.httpBasic()
    	.and()
			.logout().logoutSuccessHandler(myLogoutSuccessHandler).deleteCookies().permitAll();

		http
        .formLogin().failureUrl("/error")
        .defaultSuccessUrl("/")
        .loginPage("/login")
        .permitAll();
	}
}
