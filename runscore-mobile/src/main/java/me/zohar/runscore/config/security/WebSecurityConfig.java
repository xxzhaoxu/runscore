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

	@Autowired
	private RememberMeUserDetailsService rememberMeUserDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.exceptionHandling()
		.authenticationEntryPoint(customAuthenticationEntryPoint)
		.and()
		.csrf().disable()
		.headers().frameOptions().disable()
		.and()
		.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/pay").permitAll()
		.antMatchers("/createQRcode").permitAll()
		.antMatchers("/decodeQRcode").permitAll()
		.antMatchers("/encodeQRcode").permitAll()
		.antMatchers("/pay-online").permitAll()
		.antMatchers("/pay-online-h5").permitAll()
		.antMatchers("/paycheck").permitAll()
		.antMatchers("/payzfbcard").permitAll()
		.antMatchers("/api/**").permitAll()
		.antMatchers("/paySuccessNotice").permitAll()
		.antMatchers("/register").permitAll()
		.antMatchers("/ranking-list").permitAll()
		.antMatchers("/my-home-page").permitAll()
		.antMatchers("/masterControl/**").permitAll()
		.antMatchers("/dictconfig/findConfigValueInCache").permitAll()
		.antMatchers("/userAccount/register").permitAll()
		.antMatchers("/userAccount/getUserAccountInfo").permitAll()
		.antMatchers("/gatheringChannel/findAllGatheringChannel").permitAll()
		.antMatchers("/storage/fetch/**").permitAll()
		.antMatchers("/recharge/callback/**").permitAll()
		.antMatchers("/statisticalAnalysis/findTodayTop10BountyRank").permitAll()
		.antMatchers("/statisticalAnalysis/findTotalTop10BountyRank").permitAll()
		.anyRequest().authenticated()
		.and().formLogin().loginPage("/login").loginProcessingUrl("/login")
		.successHandler(successHandler).failureHandler(failHandler).permitAll()
		.and().rememberMe()
		.rememberMeParameter("rememberMe")
		.tokenValiditySeconds(3600 * 24 * 7)
		.userDetailsService(rememberMeUserDetailsService)
		.and().logout().logoutUrl("/logout").logoutSuccessHandler(logoutHandler).permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/alipayCard/**","/app/**","/css/**", "/images/**", "/js/**", "/plugins/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.authenticationProvider(customAuthenticationProvider);
	}

	@Bean
	public FilterRegistrationBean<Filter> updateLastAccessTimeFilter() {
		FilterRegistrationBean<Filter> updateLastAccessTimeFilter = new FilterRegistrationBean<Filter>(
				new UpdateLastAccessTimeFilter());
		updateLastAccessTimeFilter.setOrder(2);
		updateLastAccessTimeFilter.setEnabled(true);
		updateLastAccessTimeFilter.addUrlPatterns("/*");
		return updateLastAccessTimeFilter;
	}


	//过滤器，主要是过滤账号为禁止状态的用户，就跳转到登录页面
	/*@Bean
	public FilterRegistrationBean<Filter> LastAccessTimeFilter() {
		FilterRegistrationBean<Filter> userAccessFilter = new FilterRegistrationBean<Filter>(
				new UserAccessFilter());
		userAccessFilter.setOrder(1);
		userAccessFilter.setEnabled(true);
		userAccessFilter.addUrlPatterns("/*");
		return userAccessFilter;
	}*/

}
