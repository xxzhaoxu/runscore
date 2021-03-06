package me.zohar.runscore.rechargewithdraw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.rechargewithdraw.param.LowerLevelWithdrawRecordQueryCondParam;
import me.zohar.runscore.rechargewithdraw.param.StartWithdrawParam;
import me.zohar.runscore.rechargewithdraw.service.WithdrawService;
import me.zohar.runscore.useraccount.vo.UserAccountDetails;

@Controller
@RequestMapping("/withdraw")
public class WithdrawController {

	@Autowired
	private WithdrawService withdrawService;

	@PostMapping("/startWithdrawWithBankCard")
	@ResponseBody
	public Result startWithdrawWithBankCard(StartWithdrawParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		withdrawService.startWithdrawWithBankCard(param);
		return Result.success();
	}
	
	@PostMapping("/startWithdrawWithVirtualWallet")
	@ResponseBody
	public Result startWithdrawWithVirtualWallet(StartWithdrawParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		withdrawService.startWithdrawWithVirtualWallet(param);
		return Result.success();
	}

	@GetMapping("/findLowerLevelWithdrawRecordByPage")
	@ResponseBody
	public Result findLowerLevelWithdrawRecordByPage(LowerLevelWithdrawRecordQueryCondParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setCurrentAccountId(user.getUserAccountId());
		return Result.success().setData(withdrawService.findLowerLevelWithdrawRecordByPage(param));
	}

}
