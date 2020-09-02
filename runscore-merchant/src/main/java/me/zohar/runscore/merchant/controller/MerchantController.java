package me.zohar.runscore.merchant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.merchant.param.AddOrUpdateMerchantBankCardParam;
import me.zohar.runscore.merchant.param.ApplySettlementParam;
import me.zohar.runscore.merchant.param.MerchantSettlementRecordQueryCondParam;
import me.zohar.runscore.merchant.param.ModifyLoginPwdParam;
import me.zohar.runscore.merchant.param.ModifyMoneyPwdParam;
import me.zohar.runscore.merchant.service.MerchantService;
import me.zohar.runscore.merchant.vo.MerchantBankCardVO;
import me.zohar.runscore.merchant.vo.MerchantVO;
import me.zohar.runscore.useraccount.vo.MerchantAccountDetails;

@Controller
@RequestMapping("/merchant")
public class MerchantController {

	@Autowired
	private MerchantService merchantService;

	@GetMapping("/findMerchantSettlementRecordByPage")
	@ResponseBody
	public Result findMerchantSettlementRecordByPage(MerchantSettlementRecordQueryCondParam param) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setMerchantId(user.getMerchantId());
		return Result.success().setData(merchantService.findMerchantSettlementRecordByPage(param));
	}

	@PostMapping("/applySettlement")
	@ResponseBody
	public Result applySettlement(ApplySettlementParam param) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setMerchantId(user.getMerchantId());
		merchantService.applySettlement(param);
		return Result.success();
	}

	@GetMapping("/getMerchantInfo")
	@ResponseBody
	public Result getMerchantInfo() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(principal)) {
			return Result.success();
		}
		MerchantAccountDetails user = (MerchantAccountDetails) principal;
		MerchantVO merchantInfo = merchantService.getMerchantInfo(user.getMerchantId());
		return Result.success().setData(merchantInfo);
	}

	@PostMapping("/modifyLoginPwd")
	@ResponseBody
	public Result modifyLoginPwd(ModifyLoginPwdParam param) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setMerchantId(user.getMerchantId());
		merchantService.modifyLoginPwd(param);
		return Result.success();
	}

	@PostMapping("/modifyMoneyPwd")
	@ResponseBody
	public Result modifyMoneyPwd(ModifyMoneyPwdParam param) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setMerchantId(user.getMerchantId());
		merchantService.modifyMoneyPwd(param);
		return Result.success();
	}

	@GetMapping("/findMerchantBankCardById")
	@ResponseBody
	public Result findMerchantBankCardById(String id) {
		MerchantBankCardVO merchantBankCard = merchantService.findMerchantBankCardById(id);
		return Result.success().setData(merchantBankCard);
	}

	@GetMapping("/findMerchantBankCardByMerchantId")
	@ResponseBody
	public Result findMerchantBankCardByMerchantId(String merchantId) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(merchantService.findMerchantBankCardByMerchantId(user.getMerchantId()));
	}

	@PostMapping("/addOrUpdateMerchantBankCard")
	@ResponseBody
	public Result addOrUpdateMerchantBankCard(AddOrUpdateMerchantBankCardParam param) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		merchantService.addOrUpdateMerchantBankCard(param, user.getMerchantId());
		return Result.success();
	}

	@GetMapping("/deleteMerchantBankCard")
	@ResponseBody
	public Result deleteMerchantBankCard(String merchantBankCardId) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		merchantService.deleteMerchantBankCard(merchantBankCardId, user.getMerchantId());
		return Result.success();
	}

}
