package me.zohar.runscore.config.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.mastercontrol.domain.SystemSetting;
import me.zohar.runscore.mastercontrol.repo.SystemSettingRepo;
import me.zohar.runscore.useraccount.service.LoginLogService;
import me.zohar.runscore.useraccount.service.UserAccountService;
import me.zohar.runscore.useraccount.vo.UserAccountDetails;

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
	private UserAccountService userAccountService;

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private SystemSettingRepo systemSettingRepo;

	@Autowired
	private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		loginLogService.recordLoginLog(RequestContextHolder.currentRequestAttributes().getSessionId(),
				user.getUsername(), Constant.系统_会员端, Constant.登录状态_成功, Constant.登录提示_登录成功,
				HttpUtil.getClientIP(request), UserAgentUtil.parse(request.getHeader("User-Agent")));
		userAccountService.updateLatelyLoginTime(user.getUserAccountId());
		SystemSetting systemSetting = systemSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (systemSetting.getSingleDeviceLoginFlag()) {
			String currentSessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
			Map<String, ? extends Session> sessionMap = sessionRepository.findByPrincipalName(user.getUsername());
			for (Entry<String, ? extends Session> entry : sessionMap.entrySet()) {
				if (!currentSessionId.equals(entry.getKey())) {
					loginLogService.logout(entry.getKey());
				}
			}
		}

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(JSONObject.toJSONString(Result.success().setMsg(Constant.登录提示_登录成功)));
		out.flush();
		out.close();
	}
}
