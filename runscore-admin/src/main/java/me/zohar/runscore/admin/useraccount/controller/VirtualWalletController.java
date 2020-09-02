package me.zohar.runscore.admin.useraccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.useraccount.param.AddOrUpdateVirtualWalletParam;
import me.zohar.runscore.useraccount.service.VirtualWalletService;
import me.zohar.runscore.useraccount.vo.UserAccountDetails;

@Controller
@RequestMapping("/virtualWallet")
public class VirtualWalletController {

	@Autowired
	private VirtualWalletService virtualWalletService;

	@GetMapping("/delVirtualWallet")
	@ResponseBody
	public Result delVirtualWallet(String id) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		virtualWalletService.delVirtualWallet(id, user.getUserAccountId());
		return Result.success();
	}


	@GetMapping("/findVirtualWalletByUserAccountId")
	@ResponseBody
	public Result findVirtualWalletByUserAccountId(String userAccountId) {
		return Result.success().setData(virtualWalletService.findVirtualWalletByUserAccountId(userAccountId));
	}

	@PostMapping("/addOrUpdateVirtualWallet")
	@ResponseBody
	public Result addOrUpdateVirtualWallet(AddOrUpdateVirtualWalletParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		virtualWalletService.addOrUpdateVirtualWallet(param, user.getUserAccountId());
		return Result.success();
	}

}
