package me.zohar.runscore.rechargewithdraw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.rechargewithdraw.param.RechargeWithdrawLogQueryCondParam;
import me.zohar.runscore.rechargewithdraw.service.RechargeWithdrawLogService;
import me.zohar.runscore.useraccount.vo.UserAccountDetails;

@Controller
@RequestMapping("/rechargeWithdrawLog")
public class RechargeWithdrawLogController {

	@Autowired
	private RechargeWithdrawLogService rechargeWithdrawLogService;

	@GetMapping("/findMyRechargeWithdrawLogByPage")
	@ResponseBody
	public Result findMyRechargeWithdrawLogByPage(RechargeWithdrawLogQueryCondParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		return Result.success().setData(rechargeWithdrawLogService.findMyRechargeWithdrawLogByPage(param));
	}

}
