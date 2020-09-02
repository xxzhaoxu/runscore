package me.zohar.runscore.merchant.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.merchant.param.ManualStartOrderParam;
import me.zohar.runscore.merchant.param.MerchantOrderQueryCondParam;
import me.zohar.runscore.merchant.service.MerchantOrderService;
import me.zohar.runscore.useraccount.vo.MerchantAccountDetails;

@Controller
@RequestMapping("/merchantOrder")
public class MerchantOrderController {

	@Autowired
	private MerchantOrderService merchantOrderService;

	@GetMapping("/merchantCancelOrder")
	@ResponseBody
	public Result merchantCancelOrder(String id) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		merchantOrderService.cancelOrderWithMerchant(id, user.getMerchantId());
		return Result.success();
	}

	@GetMapping("/findMerchantOrderByPage")
	@ResponseBody
	public Result findMerchantOrderByPage(MerchantOrderQueryCondParam param) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setMerchantName(user.getMerchantName());
		return Result.success().setData(merchantOrderService.findMerchantOrderByPage(param));
	}

	@GetMapping("/findMerchantOrderDetailsById")
	@ResponseBody
	public Result findMerchantOrderDetailsById(String orderId) {
		return Result.success().setData(merchantOrderService.findMerchantOrderDetailsById(orderId));
	}

	@PostMapping("/startOrder")
	@ResponseBody
	public Result startOrder(@RequestBody List<ManualStartOrderParam> params) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		for (ManualStartOrderParam param : params) {
			param.setMerchantNum(user.getMerchantNum());
		}
		merchantOrderService.manualStartOrder(params);
		return Result.success();
	}

	@GetMapping("/resendNotice")
	@ResponseBody
	public Result resendNotice(String id) {
		return Result.success().setData(merchantOrderService.paySuccessAsynNotice(id));
	}

	@PostMapping("/updateNote")
	@ResponseBody
	public Result updateNote(String id, String note) {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		merchantOrderService.updateNote(id, note, user.getMerchantId());
		return Result.success();
	}
}
