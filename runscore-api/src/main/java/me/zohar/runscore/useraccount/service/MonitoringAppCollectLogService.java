package me.zohar.runscore.useraccount.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotBlank;

import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.common.valid.ParamValid;
import me.zohar.runscore.common.vo.PageResult;
import me.zohar.runscore.merchant.param.AppConfirmToPaidParam;
import me.zohar.runscore.useraccount.domain.MonitoringAppCollectLog;
import me.zohar.runscore.useraccount.domain.UserAccount;
import me.zohar.runscore.useraccount.param.MonitoringAppCollectLogQueryCondParam;
import me.zohar.runscore.useraccount.repo.MonitoringAppCollectLogRepo;
import me.zohar.runscore.useraccount.repo.UserAccountRepo;
import me.zohar.runscore.useraccount.vo.MonitoringAppCollectLogVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

@Service
public class MonitoringAppCollectLogService {

	public static final String 淘宝IP查询地址 = "http://ip.taobao.com/service/getIpInfo.php";

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private MonitoringAppCollectLogRepo monitoringAppCollectLogRepo;

	@Transactional(readOnly = true)
	public PageResult<MonitoringAppCollectLogVO> findMonitoringAppCollectLogByPage(
			MonitoringAppCollectLogQueryCondParam param) {
		Specification<MonitoringAppCollectLog> spec = new Specification<MonitoringAppCollectLog>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<MonitoringAppCollectLog> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getUserName())) {
					predicates.add(builder.equal(root.get("userAccount").get("userName"), param.getUserName()));
				}
				if (param.getStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("createTime").as(Date.class),
							DateUtil.beginOfDay(param.getStartTime())));
				}
				if (param.getEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("createTime").as(Date.class),
							DateUtil.endOfDay(param.getEndTime())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<MonitoringAppCollectLog> result = monitoringAppCollectLogRepo.findAll(spec, PageRequest
				.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<MonitoringAppCollectLogVO> pageResult = new PageResult<>(
				MonitoringAppCollectLogVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	public void heartbeatWithRedis(@NotBlank String heartbeatLogId) {
		redisTemplate.opsForValue().set("monitoringAppHeartbeat_" + heartbeatLogId,
				DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN), 80, TimeUnit.SECONDS);
	}


	@ParamValid
	@Transactional
	public void createCollectLog(AppConfirmToPaidParam param) {
		UserAccount userAccount = userAccountRepo.findBySecretKeyAndDeletedFlagIsFalse(param.getSecretKey());
		if (userAccount == null) {
			throw new BizException(BizError.账号密钥无效);
		}
		MonitoringAppCollectLog po = new MonitoringAppCollectLog();
		po.setId(IdUtils.getId());
		po.setAmount(param.getAmount());
		po.setCreateTime(new Date());
		po.setAccountId(userAccount.getId());
		if("1".equals(param.getType())){
			po.setWechatPayee(param.getPayee());
		} else if("2".equals(param.getType())){
			po.setAlipayPayee(param.getPayee());
		}else{
			po.setBankPayee(param.getPayee());
		}
		po.setSecretKey(param.getSecretKey());

		monitoringAppCollectLogRepo.save(po);
	}

}
