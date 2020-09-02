package me.zohar.runscore.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.zohar.runscore.common.utils.SpringUtils;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.useraccount.service.UserAccountService;
import me.zohar.runscore.useraccount.vo.UserAccountDetails;
import me.zohar.runscore.useraccount.vo.UserAccountInfoVO;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class UserAccessFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String path = request.getServletPath();
		System.out.println(path);
		String[] exclusionsUrls = { ".js", ".gif", ".jpg", ".png", ".css", ".ico"  };
		for (String str : exclusionsUrls) {
			if (path.contains(str)) {
				filterChain.doFilter(request, response);
				return;
			}
		}

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(principal)) {
			filterChain.doFilter(request, response);
			return;
		}
		UserAccountDetails user = (UserAccountDetails) principal;
		System.out.println(user.getUserAccountId());
		if(user != null){
			UserAccountService userAccountService = SpringUtils.getBean(UserAccountService.class);
			UserAccountInfoVO userAccountInfo = userAccountService.getUserAccountInfo(user.getUserAccountId());
			if(userAccountInfo != null){
				if (Constant.账号状态_禁用.equals(userAccountInfo.getState())) {
					response.sendRedirect("/login");
				}
			}else{
				response.sendRedirect("/login");
			}
		}

		filterChain.doFilter(request, response);
	}
}
