package me.zohar.runscore.admin.merchant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.SecureUtil;
import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.common.operlog.OperLog;
import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.merchant.param.AddMerchantParam;
import me.zohar.runscore.merchant.param.MerchantEditParam;
import me.zohar.runscore.merchant.param.MerchantQueryCondParam;
import me.zohar.runscore.merchant.param.MerchantSettlementRecordQueryCondParam;
import me.zohar.runscore.merchant.service.MerchantService;

@Controller
@RequestMapping("/merchant")
public class MerchantController {

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@OperLog(system = "后台管理", module = "商户管理", operate = "结算审核通过")
	@GetMapping("/settlementApproved")
	@ResponseBody
	public Result settlementApproved(String id, String note) {
		merchantService.settlementApproved(id, note);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "商户管理", operate = "结算审核不通过")
	@GetMapping("/settlementNotApproved")
	@ResponseBody
	public Result settlementNotApproved(String id, String note) {
		merchantService.settlementNotApproved(id, note);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "商户管理", operate = "结算确认到账")
	@GetMapping("/settlementConfirmCredited")
	@ResponseBody
	public Result settlementConfirmCredited(String id) {
		merchantService.settlementConfirmCredited(id);
		return Result.success();
	}

	@GetMapping("/findByMerchantSettlementRecordId")
	@ResponseBody
	public Result findByMerchantSettlementRecordId(String id) {
		return Result.success().setData(merchantService.findByMerchantSettlementRecordId(id));
	}

	@GetMapping("/findMerchantSettlementRecordByPage")
	@ResponseBody
	public Result findMerchantSettlementRecordByPage(MerchantSettlementRecordQueryCondParam param) {
		return Result.success().setData(merchantService.findMerchantSettlementRecordByPage(param));
	}

	@GetMapping("/findAllMerchant")
	@ResponseBody
	public Result findAllMerchant() {
		return Result.success().setData(merchantService.findAllMerchant());
	}

	@OperLog(system = "后台管理", module = "商户管理", operate = "修改登陆密码")
	@PostMapping("/modifyLoginPwd")
	@ResponseBody
	public Result modifyLoginPwd(String id, String newLoginPwd) {
		demoMode();
		merchantService.modifyLoginPwd(id, newLoginPwd);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "商户管理", operate = "修改二级密码")
	@PostMapping("/modifyMoneyPwd")
	@ResponseBody
	public Result modifyMoneyPwd(String id, String newMoneyPwd) {
		demoMode();
		merchantService.modifyMoneyPwd(id, newMoneyPwd);
		return Result.success();
	}

	@GetMapping("/generateSecretKey")
	@ResponseBody
	public Result generateSecretKey() {
		return Result.success().setData(SecureUtil.md5(UUID.fastUUID().toString()));
	}

	@OperLog(system = "后台管理", module = "商户管理", operate = "添加商户")
	@PostMapping("/addMerchant")
	@ResponseBody
	public Result addMerchant(AddMerchantParam param) {
		merchantService.addMerchant(param);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "商户管理", operate = "修改商户信息")
	@PostMapping("/updateMerchant")
	@ResponseBody
	public Result updateMerchant(MerchantEditParam param) {
		demoMode();
		merchantService.updateMerchant(param);
		return Result.success();
	}

	@GetMapping("/findMerchantById")
	@ResponseBody
	public Result findMerchantById(String id) {
		return Result.success().setData(merchantService.findMerchantById(id));
	}

	@OperLog(system = "后台管理", module = "商户管理", operate = "删除商户")
	@GetMapping("/delMerchantById")
	@ResponseBody
	public Result delMerchantById(String id) {
		demoMode();
		merchantService.delMerchantById(id);
		return Result.success();
	}

	@GetMapping("/findMerchantByPage")
	@ResponseBody
	public Result findPlatformOrderByPage(MerchantQueryCondParam param) {
		return Result.success().setData(merchantService.findMerchantByPage(param));
	}

	//演示模式
	public void demoMode(){
		String demoMode = redisTemplate.opsForValue().get("demoMode");
		if("1".equals(demoMode)){
			throw new BizException(BizError.演示模式无法执行该操作);
		}
	}
}
