package me.zohar.runscore.merchantorder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.merchant.service.GatheringChannelService;

@Controller
@RequestMapping("/gatheringChannel")
public class GatheringChannelController {

	@Autowired
	private GatheringChannelService gatheringChannelService;

	@GetMapping("/findAllGatheringChannel")
	@ResponseBody
	public Result findAllGatheringChannel() {
		return Result.success().setData(gatheringChannelService.findAllGatheringChannel());
	}

	@GetMapping("/findAllGatheringChannelRebate")
	@ResponseBody
	public Result findAllGatheringChannelRebate() {
		return Result.success().setData(gatheringChannelService.findAllGatheringChannelRebate());
	}

}
