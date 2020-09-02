package me.zohar.runscore.config.security;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;

	@Autowired
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

	@Autowired
	private AuthenticationSuccessHandler successHandler;

	@Autowired
	private AuthenticationFailHandler failHandler;

	@Autowired
	private LogoutHandler logoutHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.exceptionHandling()
		.authenticationEntryPoint(customAuthenticationEntryPoint)
		.and()
		.csrf().disable().headers().frameOptions().disable()
		.and()
		.authorizeRequests()
		.antMatchers("/login").permitAll()
		.antMatchers("/masterControl/getSystemSetting").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/login")
		.loginProcessingUrl("/login")
		.successHandler(successHandler)
		.failureHandler(failHandler).permitAll()
		.and().logout().logoutUrl("/logout").logoutSuccessHandler(logoutHandler).permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/images/**", "/js/**", "/plugins/**", "/audio/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.authenticationProvider(customAuthenticationProvider);
	}

	@Bean
	public FilterRegistrationBean<Filter> updateLastAccessTimeFilter() {
		FilterRegistrationBean<Filter> updateLastAccessTimeFilter = new FilterRegistrationBean<Filter>(
				new UpdateLastAccessTimeFilter());
		updateLastAccessTimeFilter.setOrder(1);
		updateLastAccessTimeFilter.setEnabled(true);
		updateLastAccessTimeFilter.addUrlPatterns("/*");
		return updateLastAccessTimeFilter;
	}

}
