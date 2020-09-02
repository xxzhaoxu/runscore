package me.zohar.runscore.admin.merchant.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.operlog.OperLog;
import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.merchant.param.ManualStartOrderParam;
import me.zohar.runscore.merchant.param.MerchantOrderQueryCondParam;
import me.zohar.runscore.merchant.service.MerchantOrderService;
import me.zohar.runscore.useraccount.vo.UserAccountDetails;

@Controller
@RequestMapping("/merchantOrder")
public class MerchantOrderController {

	@Autowired
	private MerchantOrderService merchantOrderService;

	@GetMapping("/realTimeQueueRecord")
	@ResponseBody
	public Result realTimeQueueRecord() {
		return Result.success().setData(merchantOrderService.realTimeQueueRecord());
	}

	@OperLog(system = "后台管理", module = "商户订单", operate = "补单")
	@PostMapping("/supplementOrder")
	@ResponseBody
	public Result supplementOrder(String id, Double gatheringAmount) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		merchantOrderService.supplementOrder(id, gatheringAmount, user.getUserAccountId());
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "商户订单", operate = "手动取消冻结余额")
	@GetMapping("/confirmToOrderFreezeRecord")
	@ResponseBody
	public Result confirmToOrderFreezeRecord(String orderId) {
		merchantOrderService.confirmToOrderFreezeRecord(orderId);
		return Result.success();
	}

	@GetMapping("/confirmToPaidWithUnconfirmedAutoFreeze")
	@ResponseBody
	public Result confirmToPaidWithUnconfirmedAutoFreeze(String orderId) {
		merchantOrderService.confirmToPaidWithUnconfirmedAutoFreeze(orderId);
		return Result.success();
	}

	@GetMapping("/findMerchantOrderByPage")
	@ResponseBody
	public Result findMerchantOrderByPage(MerchantOrderQueryCondParam param) {
		return Result.success().setData(merchantOrderService.findMerchantOrderByPage(param));
	}

	@GetMapping("/cancelOrderRefund")
	@ResponseBody
	public Result cancelOrderRefund(String id) {
		merchantOrderService.cancelOrderRefund(id);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "商户订单", operate = "取消订单")
	@GetMapping("/cancelOrder")
	@ResponseBody
	public Result cancelOrder(String id) {
		merchantOrderService.cancelOrder(id);
		return Result.success();
	}

	@GetMapping("/forceCancelOrder")
	@ResponseBody
	public Result forceCancelOrder(String id) {
		merchantOrderService.forceCancelOrder(id);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "商户订单", operate = "重发通知")
	@GetMapping("/resendNotice")
	@ResponseBody
	public Result resendNotice(String id) {
		return Result.success().setData(merchantOrderService.paySuccessAsynNotice(id));
	}

	@OperLog(system = "后台管理", module = "商户订单", operate = "确认已支付")
	@GetMapping("/confirmToPaid")
	@ResponseBody
	public Result confirmToPaid(String userAccountId, String orderId) {
		merchantOrderService.userConfirmToPaid(userAccountId, orderId);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "商户订单", operate = "确认已支付")
	@GetMapping("/confirmToPaidWithCancelOrderRefund")
	@ResponseBody
	public Result confirmToPaidWithCancelOrderRefund(String orderId) {
		merchantOrderService.confirmToPaidWithCancelOrderRefund(orderId);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "商户订单", operate = "新增订单")
	@PostMapping("/startOrder")
	@ResponseBody
	public Result startOrder(@RequestBody List<ManualStartOrderParam> params) {
		merchantOrderService.manualStartOrder(params);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "商户订单", operate = "修改备注")
	@PostMapping("/updateNote")
	@ResponseBody
	public Result updateNote(String id, String note) {
		merchantOrderService.updateNoteInner(id, note);
		return Result.success();
	}
}
