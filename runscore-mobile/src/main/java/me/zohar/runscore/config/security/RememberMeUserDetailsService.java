package me.zohar.runscore.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.useraccount.service.UserAccountService;
import me.zohar.runscore.useraccount.vo.LoginAccountInfoVO;
import me.zohar.runscore.useraccount.vo.UserAccountDetails;

@Service
public class RememberMeUserDetailsService implements UserDetailsService {

	@Autowired
	private UserAccountService userAccountService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginAccountInfoVO loginAccountInfo = userAccountService.getLoginAccountInfo(username);
		if (loginAccountInfo == null) {
			throw new AuthenticationServiceException("用户名或密码不正确");
		}
		if (Constant.账号状态_禁用.equals(loginAccountInfo.getState())) {
			throw new AuthenticationServiceException(Constant.登录提示_账号已被禁用);
		}
		UserAccountDetails userAccountDetails = new UserAccountDetails(loginAccountInfo);
		return userAccountDetails;
	}

}
