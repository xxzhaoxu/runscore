package me.zohar.runscore.api.controller;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.merchant.param.AppConfirmToPaidParam;
import me.zohar.runscore.merchant.param.StartOrderParam;
import me.zohar.runscore.merchant.service.MerchantOrderService;
import me.zohar.runscore.merchant.service.MerchantService;
import me.zohar.runscore.merchant.vo.OrderGatheringCodeVO;
import me.zohar.runscore.merchant.vo.PayUrlPicVO;
import me.zohar.runscore.useraccount.service.MonitoringAppCollectLogService;
import me.zohar.runscore.useraccount.service.UserAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

@Controller
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private MerchantOrderService merchantOrderService;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private MonitoringAppCollectLogService monitoringAppCollectLogService;

	@PostMapping("/startOrder")
	@ResponseBody
	public Result startOrder(StartOrderParam param) {
		return Result.success().setData(merchantOrderService.startOrder(param));
	}

	@PostMapping("/startOrderPic")
	@ResponseBody
	public Result startOrderPic(StartOrderParam param) {
		return Result.success().setData(merchantOrderService.startOrderPic(param));
	}

	@PostMapping("/payOnline")
	@ResponseBody
	public Result payOnline(StartOrderParam param,String secretKey) {
		//param.setNotifyUrl("https://www.baidu.com/");
		//param.setReturnUrl("https://www.baidu.com/");
		String sign = param.getMerchantNum() + param.getOrderNo() + param.getAmount() + param.getNotifyUrl()
				+ secretKey;
		sign = new Digester(DigestAlgorithm.MD5).digestHex(sign);
		param.setSign(sign);
		return Result.success().setData(merchantOrderService.startOrder(param));
	}

	@GetMapping("/getOrderGatheringCode")
	@ResponseBody
	public Result getOrderGatheringCode(String orderNo) {
		OrderGatheringCodeVO vo = merchantOrderService.getOrderGatheringCode(orderNo);
		return Result.success().setData(vo);
	}

	/**
	 * 返回收款码图片地址
	 * @param orderNo
	 * @return
	 */
	@GetMapping("/payUrlPic")
	@ResponseBody
	public Result payUrlPic(String orderNo) {
		PayUrlPicVO vo = merchantOrderService.payUrlPic(orderNo);
		return Result.success().setData(vo);
	}

	@GetMapping("/getOrderMerchantCode")
	@ResponseBody
	public Result getOrderMerchantCode(String orderNo,String merchantNum,String sign) {
		OrderGatheringCodeVO vo = merchantOrderService.getOrderMerchantCode(orderNo,merchantNum,sign);
		return Result.success().setData(vo);
	}

	@GetMapping("/heartbeat")
	@ResponseBody
	public Result heartbeat(String secretKey) {
		userAccountService.heartbeat(secretKey);
		return Result.success();
	}

	@PostMapping("/appConfirmToPaid")
	@ResponseBody
	public Result appConfirmToPaid(AppConfirmToPaidParam param) {
		monitoringAppCollectLogService.createCollectLog(param);
		merchantOrderService.appConfirmToPaid(param);
		return Result.success();
	}


	@GetMapping("/getMerchantByMerchantNum")
	@ResponseBody
	public Result getMerchantByMerchantNum(String merchantNum) {
		return Result.success().setData(merchantService.getMerchantByMerchantNum(merchantNum));
	}


	@GetMapping("/test")
	@ResponseBody
	public String test() {
		return "success";
	}



}
