package me.zohar.runscore.admin.merchant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.common.operlog.OperLog;
import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.merchant.param.AddOrUpdateGatheringChannelParam;
import me.zohar.runscore.merchant.param.BatchSettingRateParam;
import me.zohar.runscore.merchant.param.GatheringChannelQueryCondParam;
import me.zohar.runscore.merchant.param.QuickSettingRebateParam;
import me.zohar.runscore.merchant.service.GatheringChannelService;

@Controller
@RequestMapping("/gatheringChannel")
public class GatheringChannelController {

	@Autowired
	private GatheringChannelService gatheringChannelService;

	@Autowired
	private StringRedisTemplate redisTemplate;


	@PostMapping("/addRebate")
	@ResponseBody
	public Result addRebate(String channelId, Double rebate) {
		gatheringChannelService.addRebate(channelId, rebate);
		return Result.success();
	}

	@GetMapping("/delRebate")
	@ResponseBody
	public Result delRebate(String id) {
		gatheringChannelService.delRebate(id);
		return Result.success();
	}

	@PostMapping("/resetRebate")
	@ResponseBody
	public Result resetRebate(@RequestBody QuickSettingRebateParam param) {
		gatheringChannelService.resetRebate(param);
		return Result.success();
	}

	@GetMapping("/findAllGatheringChannelRebate")
	@ResponseBody
	public Result findAllGatheringChannelRebate() {
		return Result.success().setData(gatheringChannelService.findAllGatheringChannelRebate());
	}

	@GetMapping("/findGatheringChannelRateByMerchantId")
	@ResponseBody
	public Result findGatheringChannelRateByMerchantId(String merchantId) {
		return Result.success().setData(gatheringChannelService.findGatheringChannelRateByMerchantId(merchantId));
	}

	@PostMapping("/saveGatheringChannelRate")
	@ResponseBody
	public Result saveGatheringChannelRate(@RequestBody BatchSettingRateParam param) {
		gatheringChannelService.saveGatheringChannelRate(param.getMerchantId(), param.getChannelRates());
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "收款通道", operate = "新增通道")
	@PostMapping("/addOrUpdateGatheringChannel")
	@ResponseBody
	public Result addOrUpdateGatheringChannel(AddOrUpdateGatheringChannelParam param) {
		demoMode();
		gatheringChannelService.addOrUpdateGatheringChannel(param);
		return Result.success();
	}

	@GetMapping("/findGatheringChannelById")
	@ResponseBody
	public Result findGatheringChannelById(String id) {
		return Result.success().setData(gatheringChannelService.findGatheringChannelById(id));
	}

	@OperLog(system = "后台管理", module = "收款通道", operate = "删除通道")
	@GetMapping("/delGatheringChannelById")
	@ResponseBody
	public Result delGatheringChannelById(String id) {
		demoMode();
		gatheringChannelService.delGatheringChannelById(id);
		return Result.success();
	}

	@GetMapping("/findGatheringChannelByPage")
	@ResponseBody
	public Result findGatheringChannelByPage(GatheringChannelQueryCondParam param) {
		return Result.success().setData(gatheringChannelService.findGatheringChannelByPage(param));
	}

	@GetMapping("/findAllGatheringChannel")
	@ResponseBody
	public Result findAllGatheringChannel() {
		return Result.success().setData(gatheringChannelService.findAllGatheringChannel());
	}

	//演示模式
	public void demoMode(){
		String demoMode = redisTemplate.opsForValue().get("demoMode");
		if("1".equals(demoMode)){
			throw new BizException(BizError.演示模式无法执行该操作);
		}
	}
}
