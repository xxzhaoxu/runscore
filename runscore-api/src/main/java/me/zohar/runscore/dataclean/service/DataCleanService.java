package me.zohar.runscore.dataclean.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DateUtil;
import me.zohar.runscore.agent.domain.InviteCode;
import me.zohar.runscore.agent.repo.InviteCodeChannelRebateRepo;
import me.zohar.runscore.agent.repo.InviteCodeRepo;
import me.zohar.runscore.common.valid.ParamValid;
import me.zohar.runscore.dataclean.param.DataCleanParam;
import me.zohar.runscore.gatheringcode.repo.GatheringCodeRepo;
import me.zohar.runscore.merchant.repo.ActualIncomeRecordRepo;
import me.zohar.runscore.merchant.repo.AppealRepo;
import me.zohar.runscore.merchant.repo.MerchantOrderPayInfoRepo;
import me.zohar.runscore.merchant.repo.MerchantOrderRepo;
import me.zohar.runscore.merchant.repo.MerchantSettlementRecordRepo;
import me.zohar.runscore.merchant.repo.OrderRebateRepo;
import me.zohar.runscore.rechargewithdraw.repo.RechargeOrderRepo;
import me.zohar.runscore.rechargewithdraw.repo.WithdrawRecordRepo;
import me.zohar.runscore.useraccount.repo.AccountChangeLogRepo;
import me.zohar.runscore.useraccount.repo.LoginLogRepo;
import me.zohar.runscore.useraccount.repo.OperLogRepo;

@Service
public class DataCleanService {

	@Autowired
	private MerchantOrderRepo merchantOrderRepo;

	@Autowired
	private MerchantOrderPayInfoRepo merchantOrderPayInfoRepo;

	@Autowired
	private ActualIncomeRecordRepo actualIncomeRecordRepo;

	@Autowired
	private OrderRebateRepo orderRebateRepo;

	@Autowired
	private AppealRepo appealRepo;

	@Autowired
	private MerchantSettlementRecordRepo merchantSettlementRecordRepo;

	@Autowired
	private RechargeOrderRepo rechargeOrderRepo;

	@Autowired
	private WithdrawRecordRepo withdrawRecordRepo;

	@Autowired
	private InviteCodeRepo inviteCodeRepo;

	@Autowired
	private InviteCodeChannelRebateRepo inviteCodeChannelRebateRepo;

	@Autowired
	private GatheringCodeRepo gatheringCodeRepo;

	@Autowired
	private AccountChangeLogRepo accountChangeLogRepo;

	@Autowired
	private LoginLogRepo loginLogRepo;

	@Autowired
	private OperLogRepo operLogRepo;

	@ParamValid
	@Transactional
	public void dataClean(DataCleanParam param) {
		List<String> dataTypes = param.getDataTypes();
		Date startTime = DateUtil.beginOfDay(param.getStartTime());
		Date endTime = DateUtil.endOfDay(param.getEndTime());
		if (dataTypes.contains("merchantOrder")) {
			merchantOrderPayInfoRepo.deleteByNoticeTimeGreaterThanEqualAndNoticeTimeLessThanEqual(startTime, endTime);
			actualIncomeRecordRepo.deleteByCreateTimeGreaterThanEqualAndCreateTimeLessThanEqual(startTime, endTime);
			orderRebateRepo.deleteByCreateTimeGreaterThanEqualAndCreateTimeLessThanEqual(startTime, endTime);
			merchantOrderRepo.deleteBySubmitTimeGreaterThanEqualAndSubmitTimeLessThanEqual(startTime, endTime);
		}
		if (dataTypes.contains("appeal")) {
			appealRepo.deleteByInitiationTimeGreaterThanEqualAndInitiationTimeLessThanEqual(startTime, endTime);
		}
		if (dataTypes.contains("merchantSettlementRecord")) {
			merchantSettlementRecordRepo.deleteByApplyTimeGreaterThanEqualAndApplyTimeLessThanEqual(startTime, endTime);
		}
		if (dataTypes.contains("rechargeOrder")) {
			rechargeOrderRepo.deleteBySubmitTimeGreaterThanEqualAndSubmitTimeLessThanEqual(startTime, endTime);
		}
		if (dataTypes.contains("withdrawRecord")) {
			withdrawRecordRepo.deleteBySubmitTimeGreaterThanEqualAndSubmitTimeLessThanEqual(startTime, endTime);
		}
		if (dataTypes.contains("inviteCode")) {
			List<InviteCode> inviteCodes = inviteCodeRepo
					.findByCreateTimeGreaterThanEqualAndCreateTimeLessThanEqual(startTime, endTime);
			for (InviteCode inviteCode : inviteCodes) {
				inviteCodeChannelRebateRepo.deleteAll(inviteCode.getInviteCodeChannelRebates());
			}
			inviteCodeRepo.deleteAll(inviteCodes);
		}
		if (dataTypes.contains("gatheringCode")) {
			gatheringCodeRepo.deleteByCreateTimeGreaterThanEqualAndCreateTimeLessThanEqual(startTime, endTime);
		}
		if (dataTypes.contains("accountChangeLog")) {
			accountChangeLogRepo.deleteByAccountChangeTimeGreaterThanEqualAndAccountChangeTimeLessThanEqual(startTime,
					endTime);
		}
		if (dataTypes.contains("loginLog")) {
			loginLogRepo.deleteByLoginTimeGreaterThanEqualAndLoginTimeLessThanEqual(startTime, endTime);
		}
		if (dataTypes.contains("operLog")) {
			operLogRepo.deleteByOperTimeGreaterThanEqualAndOperTimeLessThanEqual(startTime, endTime);
		}
	}

}
