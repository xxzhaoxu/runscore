package me.zohar.runscore.config.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.merchant.service.MerchantService;
import me.zohar.runscore.useraccount.service.LoginLogService;
import me.zohar.runscore.useraccount.vo.MerchantAccountDetails;

/**
 * 登录成功处理类
 * 
 * @author zohar
 * @date 2019年1月23日
 *
 */
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private LoginLogService loginLogService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		loginLogService.recordLoginLog(RequestContextHolder.currentRequestAttributes().getSessionId(),
				user.getUsername(), Constant.系统_商户端, Constant.登录状态_成功, Constant.登录提示_登录成功,
				HttpUtil.getClientIP(request), UserAgentUtil.parse(request.getHeader("User-Agent")));
		merchantService.updateLatelyLoginTime(user.getMerchantId());

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(JSONObject.toJSONString(Result.success().setMsg("登录成功")));
		out.flush();
		out.close();
	}
}
