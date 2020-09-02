package me.zohar.runscore.statisticalanalysis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import me.zohar.runscore.common.valid.ParamValid;
import me.zohar.runscore.common.vo.PageResult;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.merchant.domain.MerchantOrder;
import me.zohar.runscore.merchant.param.MerchantOrderQueryCondParam;
import me.zohar.runscore.merchant.repo.MerchantOrderRepo;
import me.zohar.runscore.statisticalanalysis.domain.merchant.MerchantChannelTradeSituation;
import me.zohar.runscore.statisticalanalysis.domain.merchant.MerchantEverydayStatistical;
import me.zohar.runscore.statisticalanalysis.domain.merchant.MerchantTradeResultsSituation;
import me.zohar.runscore.statisticalanalysis.domain.merchant.MerchantTradeSituation;
import me.zohar.runscore.statisticalanalysis.param.MerchantIndexQueryParam;
import me.zohar.runscore.statisticalanalysis.param.MerchantOrderAnalysisCondParam;
import me.zohar.runscore.statisticalanalysis.repo.merchant.MerchantChannelTradeSituationRepo;
import me.zohar.runscore.statisticalanalysis.repo.merchant.MerchantEverydayStatisticalRepo;
import me.zohar.runscore.statisticalanalysis.repo.merchant.MerchantTradeResultsSituationRepo;
import me.zohar.runscore.statisticalanalysis.repo.merchant.MerchantTradeSituationRepo;
import me.zohar.runscore.statisticalanalysis.vo.IndexStatisticalVO;
import me.zohar.runscore.statisticalanalysis.vo.MerchantChannelTradeSituationVO;
import me.zohar.runscore.statisticalanalysis.vo.MerchantTradeResultsSituationVO;
import me.zohar.runscore.statisticalanalysis.vo.MerchantTradeSituationVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class MerchantStatisticalAnalysisService {

	@Autowired
	private MerchantTradeSituationRepo merchantTradeSituationRepo;

	@Autowired
	private MerchantTradeResultsSituationRepo merchantTradeResultsSituationRepo;

	@Autowired
	private MerchantEverydayStatisticalRepo everydayStatisticalRepo;

	@Autowired
	private MerchantChannelTradeSituationRepo merchantChannelTradeSituationRepo;

	@Autowired
	private MerchantOrderRepo merchantOrderRepo;

	@Transactional(readOnly = true)
	public List<MerchantChannelTradeSituationVO> findMerchantChannelTradeSituationByMerchantId(String merchantId) {
		List<MerchantChannelTradeSituation> tradeSituations = merchantChannelTradeSituationRepo
				.findByMerchantId(merchantId);
		return MerchantChannelTradeSituationVO.convertFor(tradeSituations);
	}

	@Transactional(readOnly = true)
	public MerchantTradeSituationVO findMerchantTradeSituationById(String merchantId) {
		return MerchantTradeSituationVO.convertFor(merchantTradeSituationRepo.getOne(merchantId));
	}

	@Transactional(readOnly = true)
	public List<MerchantTradeSituationVO> findMerchantTradeSituation() {
		List<MerchantTradeSituation> merchantTradeSituations = merchantTradeSituationRepo.findAll();
		return MerchantTradeSituationVO.convertFor(merchantTradeSituations);
	}

	/**
	 * 代理业绩
	 * @param param
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<MerchantTradeResultsSituationVO> findMerchantTradeResultsSituation() {
		List<MerchantTradeResultsSituation> merchantTradeSituations = merchantTradeResultsSituationRepo.findAll();
		return MerchantTradeResultsSituationVO.convertFor(merchantTradeSituations);
	}

	/**
	 * 代理业绩 有分页
	 * @param param
	 * @return
	 */
	@Transactional(readOnly = true)
	public PageResult<MerchantTradeResultsSituationVO> findMerchantTradeResultsSituationByPage(MerchantOrderQueryCondParam param) {
		Specification<MerchantTradeResultsSituation> spec = new Specification<MerchantTradeResultsSituation>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<MerchantTradeResultsSituation> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getReceiverUserName())) {
					predicates.add(builder.equal(root.get("userName"), param.getReceiverUserName()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<MerchantTradeResultsSituation> result = merchantTradeResultsSituationRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize()));
		PageResult<MerchantTradeResultsSituationVO> pageResult = new PageResult<>(MerchantTradeResultsSituationVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@ParamValid
	@Transactional(readOnly = true)
	public List<IndexStatisticalVO> findEverydayStatistical(MerchantIndexQueryParam param) {
		List<MerchantEverydayStatistical> statisticals = everydayStatisticalRepo
				.findByMerchantIdAndEverydayGreaterThanEqualAndEverydayLessThanEqualOrderByEveryday(
						param.getMerchantId(), DateUtil.beginOfDay(param.getStartTime()),
						DateUtil.beginOfDay(param.getEndTime()));
		return IndexStatisticalVO.convertForEvery(statisticals);
	}

	public Specification<MerchantOrder> buildMerchantOrderQueryCond(MerchantOrderAnalysisCondParam param) {
		Specification<MerchantOrder> spec = new Specification<MerchantOrder>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<MerchantOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (param.getStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.beginOfDay(param.getStartTime())));
				}
				if (param.getEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.endOfDay(param.getEndTime())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		return spec;
	}

	@Transactional(readOnly = true)
	public List<MerchantChannelTradeSituationVO> findChannelTradeSituation(MerchantOrderAnalysisCondParam param) {
		Map<String, MerchantChannelTradeSituationVO> reportMap = new HashMap<>();
		Specification<MerchantOrder> spec = buildMerchantOrderQueryCond(param);
		List<MerchantOrder> merchantOrders = merchantOrderRepo.findAll(spec);
		for (MerchantOrder merchantOrder : merchantOrders) {
			String id = merchantOrder.getGatheringChannelId();
			if (reportMap.get(id) == null) {
				MerchantChannelTradeSituationVO vo = MerchantChannelTradeSituationVO.build(id,
						merchantOrder.getGatheringChannel().getChannelName());
				reportMap.put(id, vo);
			}
			MerchantChannelTradeSituationVO vo = reportMap.get(id);
			vo.setOrderNum(vo.getOrderNum() + 1);
			if (Constant.商户订单状态_已支付.equals(merchantOrder.getOrderState())) {
				vo.setPaidOrderNum(vo.getPaidOrderNum() + 1);
				vo.setTradeAmount(vo.getTradeAmount() + merchantOrder.getGatheringAmount());
				vo.setPoundage(vo.getPoundage() + (merchantOrder.getGatheringAmount() * merchantOrder.getRate() / 100));
			}
		}
		List<MerchantChannelTradeSituationVO> vos = new ArrayList<>();
		Set<Entry<String, MerchantChannelTradeSituationVO>> entrySet = reportMap.entrySet();
		for (Entry<String, MerchantChannelTradeSituationVO> entry : entrySet) {
			MerchantChannelTradeSituationVO vo = entry.getValue();
			vo.setTradeAmount(NumberUtil.round(vo.getTradeAmount(), 4).doubleValue());
			vo.setPoundage(NumberUtil.round(vo.getPoundage(), 4).doubleValue());
			vo.setActualIncome(NumberUtil.round(vo.getTradeAmount() - vo.getPoundage(), 4).doubleValue());
			vo.setSuccessRate(
					NumberUtil.round((double) vo.getPaidOrderNum() / vo.getOrderNum() * 100, 1).doubleValue());
			vos.add(vo);
		}
		return vos;
	}

}
