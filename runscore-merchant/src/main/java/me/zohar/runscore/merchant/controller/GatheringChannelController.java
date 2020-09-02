package me.zohar.runscore.merchant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.merchant.service.GatheringChannelService;
import me.zohar.runscore.useraccount.vo.MerchantAccountDetails;

@Controller
@RequestMapping("/gatheringChannel")
public class GatheringChannelController {

	@Autowired
	private GatheringChannelService gatheringChannelService;

	@GetMapping("/findGatheringChannelRateByMerchantId")
	@ResponseBody
	public Result findGatheringChannelRateByMerchantId() {
		MerchantAccountDetails user = (MerchantAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success()
				.setData(gatheringChannelService.findGatheringChannelRateByMerchantId(user.getMerchantId()));
	}

	@GetMapping("/findAllGatheringChannelRebate")
	@ResponseBody
	public Result findAllGatheringChannelRebate() {
		return Result.success().setData(gatheringChannelService.findAllGatheringChannelRebate());
	}

	@GetMapping("/findAllGatheringChannel")
	@ResponseBody
	public Result findAllGatheringChannel() {
		return Result.success().setData(gatheringChannelService.findAllGatheringChannel());
	}

}
