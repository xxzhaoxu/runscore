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

import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.merchant.service.MerchantService;
import me.zohar.runscore.merchant.vo.LoginMerchantInfoVO;
import me.zohar.runscore.useraccount.service.LoginLogService;
import me.zohar.runscore.useraccount.vo.MerchantAccountDetails;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private LoginLogService loginLogService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		String username = token.getName();
		LoginMerchantInfoVO loginMerchantInfo = merchantService.getLoginMerchantInfo(username);
		if (loginMerchantInfo == null) {
			loginLogService.recordLoginLog(null, username,
					Constant.系统_商户端, Constant.登录状态_失败, Constant.登录提示_用户名不存在, HttpUtil.getClientIP(request),
					UserAgentUtil.parse(request.getHeader("User-Agent")));
			throw new AuthenticationServiceException("用户名或密码不正确");
		}
		if (!new BCryptPasswordEncoder().matches(token.getCredentials().toString(), loginMerchantInfo.getLoginPwd())) {
			loginLogService.recordLoginLog(null,
					loginMerchantInfo.getUserName(), Constant.系统_商户端, Constant.登录状态_失败, Constant.登录提示_用户名或密码不正确,
					HttpUtil.getClientIP(request), UserAgentUtil.parse(request.getHeader("User-Agent")));
			throw new AuthenticationServiceException(Constant.登录提示_用户名或密码不正确);
		}
		if (Constant.账号状态_禁用.equals(loginMerchantInfo.getState())) {
			loginLogService.recordLoginLog(null,
					loginMerchantInfo.getUserName(), Constant.系统_商户端, Constant.登录状态_失败, Constant.登录提示_账号已被禁用,
					HttpUtil.getClientIP(request), UserAgentUtil.parse(request.getHeader("User-Agent")));
			throw new AuthenticationServiceException(Constant.登录提示_账号已被禁用);
		}
		MerchantAccountDetails merchantAccountDetails = new MerchantAccountDetails(loginMerchantInfo);
		return new UsernamePasswordAuthenticationToken(merchantAccountDetails, merchantAccountDetails.getPassword(),
				merchantAccountDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.equals(authentication);
	}

}
