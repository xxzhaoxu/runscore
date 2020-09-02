package me.zohar.runscore.admin.mastercontrol.controller;

import java.util.List;

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
import me.zohar.runscore.mastercontrol.param.UpdateCustomerQrcodeSettingParam;
import me.zohar.runscore.mastercontrol.param.UpdateReceiveOrderSettingParam;
import me.zohar.runscore.mastercontrol.param.UpdateRechargeSettingParam;
import me.zohar.runscore.mastercontrol.param.UpdateRegisterSettingParam;
import me.zohar.runscore.mastercontrol.param.UpdateSystemSettingParam;
import me.zohar.runscore.mastercontrol.param.UpdateWithdrawSettingParam;
import me.zohar.runscore.mastercontrol.service.MasterControlService;

/**
 * 总控
 *
 * @author zohar
 * @date 2019年3月9日
 *
 */
@Controller
@RequestMapping("/masterControl")
public class MasterControlController {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private MasterControlService service;

	@GetMapping("/getSystemSetting")
	@ResponseBody
	public Result getSystemSetting() {
		return Result.success().setData(service.getSystemSetting());
	}

	@OperLog(system = "后台管理", module = "总控室", operate = "更新系统设置")
	@PostMapping("/updateSystemSetting")
	@ResponseBody
	public Result updateSystemSetting(UpdateSystemSettingParam param) {
		demoMode();
		service.updateSystemSetting(param);
		return Result.success();
	}

	@GetMapping("/getRegisterSetting")
	@ResponseBody
	public Result getRegisterSetting() {
		return Result.success().setData(service.getRegisterSetting());
	}

	@OperLog(system = "后台管理", module = "总控室", operate = "更新注册代理设置")
	@PostMapping("/updateRegisterSetting")
	@ResponseBody
	public Result updateRegisterSetting(UpdateRegisterSettingParam param) {
		demoMode();
		service.updateRegisterSetting(param);
		return Result.success();
	}

	@GetMapping("/getReceiveOrderSetting")
	@ResponseBody
	public Result getReceiveOrderSetting() {
		return Result.success().setData(service.getReceiveOrderSetting());
	}

	@OperLog(system = "后台管理", module = "总控室", operate = "更新接单设置")
	@PostMapping("/updateReceiveOrderSetting")
	@ResponseBody
	public Result updateReceiveOrderSetting(UpdateReceiveOrderSettingParam param) {
		service.updateReceiveOrderSetting(param);
		return Result.success();
	}

	@GetMapping("/getRechargeSetting")
	@ResponseBody
	public Result getRechargeSetting() {
		return Result.success().setData(service.getRechargeSetting());
	}

	@OperLog(system = "后台管理", module = "总控室", operate = "更新充值设置")
	@PostMapping("/updateRechargeSetting")
	@ResponseBody
	public Result updateRechargeSetting(UpdateRechargeSettingParam param) {
		demoMode();
		service.updateRechargeSetting(param);
		return Result.success();
	}

	@GetMapping("/getWithdrawSetting")
	@ResponseBody
	public Result getWithdrawSetting() {
		return Result.success().setData(service.getWithdrawSetting());
	}

	@OperLog(system = "后台管理", module = "总控室", operate = "更新提现设置")
	@PostMapping("/updateWithdrawSetting")
	@ResponseBody
	public Result updateWithdrawSetting(UpdateWithdrawSettingParam param) {
		demoMode();
		service.updateWithdrawSetting(param);
		return Result.success();
	}

	@GetMapping("/getCustomerQrcodeSetting")
	@ResponseBody
	public Result getCustomerQrcodeSetting() {
		return Result.success().setData(service.getCustomerQrcodeSetting());
	}

	@OperLog(system = "后台管理", module = "总控室", operate = "更新客服设置")
	@PostMapping("/updateCustomerQrcodeSetting")
	@ResponseBody
	public Result updateCustomerQrcodeSetting(UpdateCustomerQrcodeSettingParam param) {
		demoMode();
		service.updateCustomerQrcodeSetting(param);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "总控室", operate = "刷新缓存")
	@PostMapping("/refreshCache")
	@ResponseBody
	public Result refreshCache(@RequestBody List<String> cacheItems) {
		service.refreshCache(cacheItems);
		return Result.success();
	}

	//演示模式
	public void demoMode(){
		String demoMode = redisTemplate.opsForValue().get("demoMode");
		if("1".equals(demoMode)){
			throw new BizException(BizError.演示模式无法执行该操作);
		}
	}

}
