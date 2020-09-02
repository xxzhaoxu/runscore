package me.zohar.runscore.statisticalanalysis.service;

import java.util.List;

import javax.validation.constraints.NotBlank;

import me.zohar.runscore.statisticalanalysis.domain.AdjustCashDepositSituation;
import me.zohar.runscore.statisticalanalysis.domain.CashDepositBounty;
import me.zohar.runscore.statisticalanalysis.domain.RechargeSituation;
import me.zohar.runscore.statisticalanalysis.domain.TodayAccountReceiveOrderSituation;
import me.zohar.runscore.statisticalanalysis.domain.TotalAccountReceiveOrderSituation;
import me.zohar.runscore.statisticalanalysis.domain.TotalAccountReceiveResultsSituation;
import me.zohar.runscore.statisticalanalysis.domain.TradeSituation;
import me.zohar.runscore.statisticalanalysis.domain.WithdrawSituation;
import me.zohar.runscore.statisticalanalysis.repo.AdjustCashDepositSituationRepo;
import me.zohar.runscore.statisticalanalysis.repo.CashDepositBountyRepo;
import me.zohar.runscore.statisticalanalysis.repo.RechargeSituationRepo;
import me.zohar.runscore.statisticalanalysis.repo.TodayAccountReceiveOrderSituationRepo;
import me.zohar.runscore.statisticalanalysis.repo.TotalAccountReceiveOrderSituationRepo;
import me.zohar.runscore.statisticalanalysis.repo.TotalAccountReceiveResultsSituationRepo;
import me.zohar.runscore.statisticalanalysis.repo.TradeSituationRepo;
import me.zohar.runscore.statisticalanalysis.repo.WithdrawSituationRepo;
import me.zohar.runscore.statisticalanalysis.vo.AccountReceiveOrderSituationVO;
import me.zohar.runscore.statisticalanalysis.vo.AccountReceiveResultsSituationVO;
import me.zohar.runscore.statisticalanalysis.vo.AdjustCashDepositSituationVO;
import me.zohar.runscore.statisticalanalysis.vo.BountyRankVO;
import me.zohar.runscore.statisticalanalysis.vo.CashDepositBountyVO;
import me.zohar.runscore.statisticalanalysis.vo.RechargeSituationVO;
import me.zohar.runscore.statisticalanalysis.vo.TradeSituationVO;
import me.zohar.runscore.statisticalanalysis.vo.WithdrawSituationVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.alicp.jetcache.anno.Cached;

@Validated
@Service
public class StatisticalAnalysisService {

	@Autowired
	private TotalAccountReceiveOrderSituationRepo totalAccountReceiveOrderSituationRepo;

	@Autowired
	private TotalAccountReceiveResultsSituationRepo totalAccountReceiveResultsSituationRepo;

	@Autowired
	private TodayAccountReceiveOrderSituationRepo todayAccountReceiveOrderSituationRepo;


	@Autowired
	private CashDepositBountyRepo cashDepositBountyRepo;

	@Autowired
	private TradeSituationRepo tradeSituationRepo;

	@Autowired
	private WithdrawSituationRepo withdrawSituationRepo;

	@Autowired
	private RechargeSituationRepo rechargeSituationRepo;

	@Autowired
	private AdjustCashDepositSituationRepo adjustCashDepositSituationRepo;

	@Transactional(readOnly = true)
	public AccountReceiveOrderSituationVO findMyTodayReceiveOrderSituation(@NotBlank String userAccountId) {
		return AccountReceiveOrderSituationVO
				.convertForToday(todayAccountReceiveOrderSituationRepo.findByReceivedAccountId(userAccountId));
	}

	@Transactional(readOnly = true)
	public AccountReceiveOrderSituationVO findMyTotalReceiveOrderSituation(@NotBlank String userAccountId) {
		return AccountReceiveOrderSituationVO
				.convertForTotal(totalAccountReceiveOrderSituationRepo.findByReceivedAccountId(userAccountId));
	}

	@Transactional(readOnly = true)
	public List<AccountReceiveResultsSituationVO> findMyTotalReceiveResultsSituation(@NotBlank String inviterId) {
		List<TotalAccountReceiveResultsSituation> receiveOrderSituations = totalAccountReceiveResultsSituationRepo
				.findByInviterIdAndDeletedFlagIsFalse(inviterId);
		return AccountReceiveResultsSituationVO.convertForTotal(receiveOrderSituations);
	}


	@Cached(name = "totalTop10BountyRank", expire = 300)
	@Transactional(readOnly = true)
	public List<BountyRankVO> findTotalTop10BountyRank() {
		List<TotalAccountReceiveOrderSituation> receiveOrderSituations = totalAccountReceiveOrderSituationRepo
				.findTop10ByOrderByBountyDesc();
		return BountyRankVO.convertFor(receiveOrderSituations);
	}

	@Cached(name = "todayTop10BountyRank", expire = 300)
	@Transactional(readOnly = true)
	public List<BountyRankVO> findTodayTop10BountyRank() {
		List<TodayAccountReceiveOrderSituation> todayReceiveOrderSituations = todayAccountReceiveOrderSituationRepo
				.findTop10ByOrderByBountyDesc();
		return BountyRankVO.convertForToday(todayReceiveOrderSituations);
	}

	@Transactional(readOnly = true)
	public CashDepositBountyVO findCashDepositBounty() {
		CashDepositBounty cashDepositBounty = cashDepositBountyRepo.findTopBy();
		return CashDepositBountyVO.convertFor(cashDepositBounty);
	}

	@Transactional(readOnly = true)
	public TradeSituationVO findTradeSituation() {
		TradeSituation tradeSituation = tradeSituationRepo.findTopBy();
		return TradeSituationVO.convertFor(tradeSituation);
	}

	@Transactional(readOnly = true)
	public RechargeSituationVO findRechargeSituation() {
		RechargeSituation situation = rechargeSituationRepo.findTopBy();
		return RechargeSituationVO.convertFor(situation);
	}

	@Transactional(readOnly = true)
	public AdjustCashDepositSituationVO findAdjustCashDepositSituation() {
		AdjustCashDepositSituation situation = adjustCashDepositSituationRepo.findTopBy();
		return AdjustCashDepositSituationVO.convertFor(situation);
	}

	@Transactional(readOnly = true)
	public WithdrawSituationVO findWithdrawSituation() {
		WithdrawSituation situation = withdrawSituationRepo.findTopBy();
		return WithdrawSituationVO.convertFor(situation);
	}

}
