package me.zohar.runscore.useraccount.controller;

import me.zohar.runscore.common.auth.GoogleAuthenticator;
import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.merchant.service.FreezeRecordService;
import me.zohar.runscore.useraccount.param.AccountChangeLogQueryCondParam;
import me.zohar.runscore.useraccount.param.LowerLevelAccountChangeLogQueryCondParam;
import me.zohar.runscore.useraccount.param.ModifyLoginPwdParam;
import me.zohar.runscore.useraccount.param.ModifyMoneyPwdParam;
import me.zohar.runscore.useraccount.param.UpdateCityInfoParam;
import me.zohar.runscore.useraccount.param.UserAccountRegisterParam;
import me.zohar.runscore.useraccount.param.UserCardParam;
import me.zohar.runscore.useraccount.service.AccountChannelRebateService;
import me.zohar.runscore.useraccount.service.UserAccountService;
import me.zohar.runscore.useraccount.vo.UserAccountDetails;
import me.zohar.runscore.useraccount.vo.UserAccountInfoVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/userAccount")
public class UserAccountController {

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private AccountChannelRebateService accountChannelRebateService;

	@Autowired
	private FreezeRecordService freezeRecordService;

	@PostMapping("/bindGoogleAuth")
	@ResponseBody
	public Result bindGoogleAuth(String googleSecretKey, String googleVerCode) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
			    .getPrincipal();
		userAccountService.bindGoogleAuth(user.getUserAccountId(), googleSecretKey, googleVerCode);
		return Result.success();
	}

	@GetMapping("/generateGoogleSecretKey")
	@ResponseBody
	public Result generateGoogleSecretKey() {
		return Result.success().setData(GoogleAuthenticator.generateSecretKey());
	}

	@GetMapping("/getGoogleAuthInfo")
	@ResponseBody
	public Result getGoogleAuthInfo() {
		 UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				    .getPrincipal();
		return Result.success().setData(userAccountService.getGoogleAuthInfo(user.getUserAccountId()));
	}

	 @PostMapping("/updateCityInfo")
	 @ResponseBody
	 public Result updateCityInfo(UpdateCityInfoParam param) {
	  UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
	    .getPrincipal();
	  param.setUserAccountId(user.getUserAccountId());
	  userAccountService.updateCityInfo(param);
	  return Result.success();
	 }

	@GetMapping("/updateSecretKey")
	@ResponseBody
	public Result updateSecretKey() {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		userAccountService.updateSecretKey(user.getUserAccountId());
		return Result.success();
	}

	@PostMapping("/updateUserCard")
	@ResponseBody
	public Result updateUserCard(@RequestBody UserCardParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if(user != null){
			userAccountService.updateUserCard(user, param);
		}
		return Result.success();
	}

	@GetMapping("/getFreezeAmount")
	@ResponseBody
	public Result getFreezeAmount() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(principal)) {
			return Result.success();
		}
		UserAccountDetails user = (UserAccountDetails) principal;
		return Result.success().setData(freezeRecordService.getFreezeAmount(user.getUserAccountId()));
	}

	@GetMapping("/findMyReceiveOrderChannel")
	@ResponseBody
	public Result findMyReceiveOrderChannel() {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(
				accountChannelRebateService.findAccountReceiveOrderChannelByAccountId(user.getUserAccountId()));
	}

	@PostMapping("/updateReceiveOrderState")
	@ResponseBody
	public Result updateReceiveOrderState(String receiveOrderState) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		userAccountService.updateReceiveOrderState(user.getUserAccountId(), receiveOrderState);
		return Result.success();
	}

	@PostMapping("/modifyLoginPwd")
	@ResponseBody
	public Result modifyLoginPwd(ModifyLoginPwdParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		userAccountService.modifyLoginPwd(param);
		return Result.success();
	}

	@PostMapping("/modifyMoneyPwd")
	@ResponseBody
	public Result modifyMoneyPwd(ModifyMoneyPwdParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		userAccountService.modifyMoneyPwd(param);
		return Result.success();
	}

	@PostMapping("/register")
	@ResponseBody
	public Result register(UserAccountRegisterParam param) {
		userAccountService.register(param);
		return Result.success();
	}

	@GetMapping("/getUserAccountInfo")
	@ResponseBody
	public Result getUserAccountInfo() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(principal)) {
			return Result.success();
		}
		UserAccountDetails user = (UserAccountDetails) principal;
		UserAccountInfoVO userAccountInfo = userAccountService.getUserAccountInfo(user.getUserAccountId());
		return Result.success().setData(userAccountInfo);
	}

	@GetMapping("/getCityInfo")
	 @ResponseBody
	 public Result getCityInfo() {
	  UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
	    .getPrincipal();
	  return Result.success().setData(userAccountService.getCityInfo(user.getUserAccountId()));
	 }

	@GetMapping("/findMyAccountChangeLogByPage")
	@ResponseBody
	public Result findMyAccountChangeLogByPage(AccountChangeLogQueryCondParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		return Result.success().setData(userAccountService.findAccountChangeLogByPage(param));
	}

	@GetMapping("/findLowerLevelAccountChangeLogByPage")
	@ResponseBody
	public Result findLowerLevelAccountChangeLogByPage(LowerLevelAccountChangeLogQueryCondParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setCurrentAccountId(user.getUserAccountId());
		return Result.success().setData(userAccountService.findLowerLevelAccountChangeLogByPage(param));
	}

}
