package me.zohar.runscore.config.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import me.zohar.runscore.common.auth.GoogleAuthenticator;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.mastercontrol.service.MasterControlService;
import me.zohar.runscore.mastercontrol.vo.SystemSettingVO;
import me.zohar.runscore.useraccount.service.LoginLogService;
import me.zohar.runscore.useraccount.service.UserAccountService;
import me.zohar.runscore.useraccount.vo.LoginAccountInfoVO;
import me.zohar.runscore.useraccount.vo.UserAccountDetails;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private MasterControlService masterControlService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		String username = token.getName();
		LoginAccountInfoVO loginAccountInfo = userAccountService.getLoginAccountInfo(username);
		if (loginAccountInfo == null) {
			loginLogService.recordLoginLog(null, username, Constant.系统_会员端, Constant.登录状态_失败, Constant.登录提示_用户名不存在,
					HttpUtil.getClientIP(request), UserAgentUtil.parse(request.getHeader("User-Agent")));
			throw new AuthenticationServiceException("用户名或密码不正确");
		}
		if (!new BCryptPasswordEncoder().matches(token.getCredentials().toString(), loginAccountInfo.getLoginPwd())) {
			loginLogService.recordLoginLog(null, loginAccountInfo.getUserName(), Constant.系统_会员端, Constant.登录状态_失败,
					Constant.登录提示_用户名或密码不正确, HttpUtil.getClientIP(request),
					UserAgentUtil.parse(request.getHeader("User-Agent")));
			throw new AuthenticationServiceException(Constant.登录提示_用户名或密码不正确);
		}
		if (Constant.账号状态_禁用.equals(loginAccountInfo.getState())) {
			loginLogService.recordLoginLog(null, loginAccountInfo.getUserName(), Constant.系统_会员端, Constant.登录状态_失败,
					Constant.登录提示_账号已被禁用, HttpUtil.getClientIP(request),
					UserAgentUtil.parse(request.getHeader("User-Agent")));
			throw new AuthenticationServiceException(Constant.登录提示_账号已被禁用);
		}
		SystemSettingVO systemSetting = masterControlService.getSystemSetting();
		if (systemSetting.getMobileLoginGoogleAuth()) {
			if (StrUtil.isNotBlank(loginAccountInfo.getGoogleSecretKey())) {
				String googleVerCode = request.getParameter("googleVerCode");
				if (StrUtil.isBlank(googleVerCode) || !GoogleAuthenticator
						.checkCode(loginAccountInfo.getGoogleSecretKey(), googleVerCode, System.currentTimeMillis())) {
					loginLogService.recordLoginLog(null, username, Constant.系统_会员端, Constant.登录状态_失败,
							Constant.登录提示_谷歌验证码不正确, HttpUtil.getClientIP(request),
							UserAgentUtil.parse(request.getHeader("User-Agent")));
					throw new AuthenticationServiceException(Constant.登录提示_谷歌验证码不正确);
				}
			}
		}
		UserAccountDetails userAccountDetails = new UserAccountDetails(loginAccountInfo);
		return new UsernamePasswordAuthenticationToken(userAccountDetails, userAccountDetails.getPassword(),
				userAccountDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
