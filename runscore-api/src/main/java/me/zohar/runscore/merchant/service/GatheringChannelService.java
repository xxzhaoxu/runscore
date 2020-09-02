package me.zohar.runscore.merchant.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import cn.hutool.core.util.StrUtil;
import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.common.valid.ParamValid;
import me.zohar.runscore.common.vo.PageResult;
import me.zohar.runscore.merchant.domain.GatheringChannel;
import me.zohar.runscore.merchant.domain.GatheringChannelRate;
import me.zohar.runscore.merchant.domain.GatheringChannelRebate;
import me.zohar.runscore.merchant.param.AddOrUpdateGatheringChannelParam;
import me.zohar.runscore.merchant.param.GatheringChannelQueryCondParam;
import me.zohar.runscore.merchant.param.GatheringChannelRateParam;
import me.zohar.runscore.merchant.param.QuickSettingRebateParam;
import me.zohar.runscore.merchant.repo.GatheringChannelRateRepo;
import me.zohar.runscore.merchant.repo.GatheringChannelRebateRepo;
import me.zohar.runscore.merchant.repo.GatheringChannelRepo;
import me.zohar.runscore.merchant.vo.GatheringChannelRateVO;
import me.zohar.runscore.merchant.vo.GatheringChannelRebateVO;
import me.zohar.runscore.merchant.vo.GatheringChannelVO;

@Validated
@Service
public class GatheringChannelService {

	@Autowired
	private GatheringChannelRepo gatheringChannelRepo;

	@Autowired
	private GatheringChannelRateRepo gatheringChannelRateRepo;

	@Autowired
	private GatheringChannelRebateRepo gatheringChannelRebateRepo;

	@Transactional
	public void addRebate(@NotBlank String channelId,
			@NotNull @DecimalMin(value = "0", inclusive = true) Double rebate) {
		GatheringChannelRebate existRebate = gatheringChannelRebateRepo.findByChannelIdAndRebate(channelId, rebate);
		if (existRebate != null) {
			throw new BizException(BizError.该返点已存在);
		}
		GatheringChannelRebate newRebate = new GatheringChannelRebate();
		newRebate.setId(IdUtils.getId());
		newRebate.setChannelId(channelId);
		newRebate.setRebate(rebate);
		newRebate.setCreateTime(new Date());
		gatheringChannelRebateRepo.save(newRebate);
	}

	@Transactional
	public void delRebate(@NotBlank String id) {
		gatheringChannelRebateRepo.deleteById(id);
	}

	@ParamValid
	@Transactional
	public void resetRebate(QuickSettingRebateParam param) {
		Map<Double, Double> map = new HashMap<>();
		for (Double rebate : param.getRebates()) {
			if (map.get(rebate) != null) {
				throw new BizException(BizError.不能设置重复的返点);
			}
			map.put(rebate, rebate);
		}
		List<GatheringChannelRebate> rebates = gatheringChannelRebateRepo.findByChannelId(param.getChannelId());
		gatheringChannelRebateRepo.deleteAll(rebates);
		Date now = new Date();
		for (Double rebate : param.getRebates()) {
			GatheringChannelRebate po = new GatheringChannelRebate();
			po.setId(IdUtils.getId());
			po.setChannelId(param.getChannelId());
			po.setRebate(rebate);
			po.setCreateTime(now);
			gatheringChannelRebateRepo.save(po);
		}
	}

	@Transactional(readOnly = true)
	public List<GatheringChannelRebateVO> findAllGatheringChannelRebate() {
		return GatheringChannelRebateVO
				.convertFor(gatheringChannelRebateRepo.findByChannelDeletedFlagIsFalseOrderByRebate());
	}

	@Transactional(readOnly = true)
	public List<GatheringChannelRateVO> findGatheringChannelRateByMerchantId(@NotBlank String merchantId) {
		return GatheringChannelRateVO.convertFor(gatheringChannelRateRepo.findByMerchantId(merchantId));
	}

	@Transactional
	public void saveGatheringChannelRate(@NotBlank String merchantId, List<GatheringChannelRateParam> params) {
		List<GatheringChannelRate> rates = gatheringChannelRateRepo.findByMerchantId(merchantId);
		for (GatheringChannelRate rate : rates) {
			gatheringChannelRateRepo.delete(rate);
		}
		Map<String, String> map = new HashMap<>();
		for (GatheringChannelRateParam param : params) {
			if (param.getMinAmount() > param.getMaxAmount()) {
				throw new BizException(BizError.通道限额范围无效);
			}
			if (map.get(param.getChannelId()) != null) {
				throw new BizException(BizError.不能设置重复的接单通道);
			}
			map.put(param.getChannelId(), param.getChannelId());
		}
		for (GatheringChannelRateParam param : params) {
			GatheringChannelRate rate = param.convertToPo(merchantId);
			gatheringChannelRateRepo.save(rate);
		}
	}

	@ParamValid
	@Transactional
	public void addOrUpdateGatheringChannel(AddOrUpdateGatheringChannelParam param) {
		GatheringChannel existGatheringChannel = gatheringChannelRepo
				.findByChannelCodeAndDeletedFlagIsFalse(param.getChannelCode());
		if (existGatheringChannel != null && !existGatheringChannel.getId().equals(param.getId())) {
			throw new BizException(BizError.收款通道已使用);
		}

		if (StrUtil.isNotBlank(param.getId())) {
			GatheringChannel gatheringChannel = gatheringChannelRepo.getOne(param.getId());
			BeanUtils.copyProperties(param, gatheringChannel);
			gatheringChannelRepo.save(gatheringChannel);
		} else {
			GatheringChannel gatheringChannel = param.convertToPo();
			gatheringChannelRepo.save(gatheringChannel);
		}
	}

	@Transactional(readOnly = true)
	public GatheringChannelVO findGatheringChannelById(@NotBlank String id) {
		return GatheringChannelVO.convertFor(gatheringChannelRepo.getOne(id));
	}

	@Transactional
	public void delGatheringChannelById(@NotBlank String id) {
		GatheringChannel gatheringChannel = gatheringChannelRepo.getOne(id);
		gatheringChannel.setDeletedFlag(true);
		gatheringChannelRepo.save(gatheringChannel);
	}

	@Transactional(readOnly = true)
	public PageResult<GatheringChannelVO> findGatheringChannelByPage(GatheringChannelQueryCondParam param) {
		Specification<GatheringChannel> spec = new Specification<GatheringChannel>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<GatheringChannel> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.get("deletedFlag"), false));
				if (StrUtil.isNotBlank(param.getChannelCode())) {
					predicates.add(builder.like(root.get("channelCode"), "%" + param.getChannelCode() + "%"));
				}
				if (StrUtil.isNotBlank(param.getChannelName())) {
					predicates.add(builder.like(root.get("channelName"), "%" + param.getChannelName() + "%"));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<GatheringChannel> result = gatheringChannelRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<GatheringChannelVO> pageResult = new PageResult<>(GatheringChannelVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public List<GatheringChannelVO> findAllGatheringChannel() {
		return GatheringChannelVO.convertFor(gatheringChannelRepo.findByDeletedFlagIsFalse());
	}

}
