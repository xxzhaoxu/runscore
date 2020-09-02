package me.zohar.runscore.admin.msg.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.vo.PageResult;
import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.gatheringcode.service.GatheringCodeService;
import me.zohar.runscore.gatheringcode.vo.GatheringCodeUsageVO;
import me.zohar.runscore.merchant.service.AppealService;
import me.zohar.runscore.merchant.service.MerchantService;
import me.zohar.runscore.merchant.vo.AppealVO;
import me.zohar.runscore.merchant.vo.MerchantSettlementRecordVO;
import me.zohar.runscore.rechargewithdraw.service.RechargeService;
import me.zohar.runscore.rechargewithdraw.service.WithdrawService;
import me.zohar.runscore.rechargewithdraw.vo.RechargeOrderVO;
import me.zohar.runscore.rechargewithdraw.vo.WithdrawRecordVO;

@Controller
@RequestMapping("/msg")
public class MsgController {

	@Autowired
	private RechargeService rechargeService;

	@Autowired
	private WithdrawService withdrawService;

	@Autowired
	private AppealService appealService;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private GatheringCodeService gatheringCodeService;

	@GetMapping("/findTodoMsg")
	@ResponseBody
	public Result findTodoMsg() {
		PageResult<RechargeOrderVO> recharge = rechargeService.findTop5TodoRechargeOrderByPage();
		PageResult<WithdrawRecordVO> withdraw = withdrawService.findTop5TodoWithdrawRecordByPage();
		PageResult<AppealVO> appeal = appealService.findTop5TodoAppealByPage(null);
		PageResult<MerchantSettlementRecordVO> settlement = merchantService.findTop5TodoSettlementByPage();
		PageResult<GatheringCodeUsageVO> auditGatheringCode = gatheringCodeService
				.findTop5TodoAuditGatheringCodeByPage();
		Map<String, Object> map = new HashMap<>();
		map.put("recharge", recharge);
		map.put("withdraw", withdraw);
		map.put("appeal", appeal);
		map.put("settlement", settlement);
		map.put("auditGatheringCode", auditGatheringCode);
		return Result.success().setData(map);
	}

}
