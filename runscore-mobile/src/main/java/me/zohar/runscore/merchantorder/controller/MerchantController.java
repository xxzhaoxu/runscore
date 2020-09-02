package me.zohar.runscore.merchantorder.controller;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.merchant.service.MerchantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/merchant")
public class MerchantController {

	@Autowired
	private MerchantService merchantService;

	@GetMapping("/getMerchantByMerchantNum")
	@ResponseBody
	public Result getMerchantByMerchantNum(String merchantNum) {
		return Result.success().setData(merchantService.getMerchantByMerchantNum(merchantNum));
	}

}
