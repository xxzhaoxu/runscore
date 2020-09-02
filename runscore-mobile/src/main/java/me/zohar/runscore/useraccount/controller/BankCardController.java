package me.zohar.runscore.useraccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.useraccount.param.AddOrUpdateBankCardParam;
import me.zohar.runscore.useraccount.service.BankCardService;
import me.zohar.runscore.useraccount.vo.UserAccountDetails;

@Controller
@RequestMapping("/bankCard")
public class BankCardController {

	@Autowired
	private BankCardService bankCardService;

	@GetMapping("/delBankCard")
	@ResponseBody
	public Result delBankCard(String id) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		bankCardService.delBankCard(id, user.getUserAccountId());
		return Result.success();
	}

	@GetMapping("/findMyBankCardById")
	@ResponseBody
	public Result findMyBankCardById(String id) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(bankCardService.findMyBankCardById(id, user.getUserAccountId()));
	}

	@GetMapping("/findMyBankCard")
	@ResponseBody
	public Result findMyBankCard() {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(bankCardService.findBankCard(user.getUserAccountId()));
	}

	@PostMapping("/addOrUpdateBankCard")
	@ResponseBody
	public Result addOrUpdateBankCard(AddOrUpdateBankCardParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		bankCardService.addOrUpdateBankCard(param, user.getUserAccountId());
		return Result.success();
	}

}
