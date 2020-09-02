package me.zohar.runscore.useraccount.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotBlank;

import lombok.extern.slf4j.Slf4j;
import me.zohar.runscore.common.vo.PageResult;
import me.zohar.runscore.useraccount.domain.MonitoringAppHeartbeatLog;
import me.zohar.runscore.useraccount.param.MonitoringAppHeartbeatLogQueryCondParam;
import me.zohar.runscore.useraccount.repo.MonitoringAppHeartbeatLogRepo;
import me.zohar.runscore.useraccount.repo.UserAccountRepo;
import me.zohar.runscore.useraccount.vo.MonitoringAppHeartbeatLogVO;

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

@Slf4j
@Service
public class MonitoringAppHeartbeatLogService {

	public static final String 淘宝IP查询地址 = "http://ip.taobao.com/service/getIpInfo.php";

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private MonitoringAppHeartbeatLogRepo monitoringAppHeartbeatLogRepo;

	@Transactional(readOnly = true)
	public PageResult<MonitoringAppHeartbeatLogVO> findMonitoringAppHeartbeatLogByPage(
			MonitoringAppHeartbeatLogQueryCondParam param) {
		Specification<MonitoringAppHeartbeatLog> spec = new Specification<MonitoringAppHeartbeatLog>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<MonitoringAppHeartbeatLog> root, CriteriaQuery<?> query,
					CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getIpAddr())) {
					predicates.add(builder.equal(root.get("ipAddr"), param.getIpAddr()));
				}
				if (StrUtil.isNotEmpty(param.getUserName())) {
					predicates.add(builder.equal(root.get("userName"), param.getUserName()));
				}
				if (param.getStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("lastHeartbeatTime").as(Date.class),
							DateUtil.beginOfDay(param.getStartTime())));
				}
				if (param.getEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("lastHeartbeatTime").as(Date.class),
							DateUtil.endOfDay(param.getEndTime())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<MonitoringAppHeartbeatLog> result = monitoringAppHeartbeatLogRepo.findAll(spec, PageRequest
				.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("lastHeartbeatTime"))));
		PageResult<MonitoringAppHeartbeatLogVO> pageResult = new PageResult<>(
				MonitoringAppHeartbeatLogVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	public void heartbeatWithRedis(@NotBlank String heartbeatLogId) {
		redisTemplate.opsForValue().set("monitoringAppHeartbeat_" + heartbeatLogId,
				DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN), 80, TimeUnit.SECONDS);
	}

	@Transactional
	public void updateLastHeartbeatTime() {
		String prefix = "monitoringAppHeartbeat_";
		Set<String> keys = redisTemplate.keys(prefix + "*");
		for (String key : keys) {
			String sessionId = key.split(prefix)[1];
			updateLastHeartbeatTime(sessionId,
					DateUtil.parse(redisTemplate.opsForValue().get(key), DatePattern.NORM_DATETIME_PATTERN));
		}
	}

	@Transactional
	public void updateLastHeartbeatTime(String heartbeatLogId, Date lastHeartbeatTime) {
		MonitoringAppHeartbeatLog monitoringAppHeartbeatLog = monitoringAppHeartbeatLogRepo.getOne(heartbeatLogId);
		if (monitoringAppHeartbeatLog != null) {
			if (monitoringAppHeartbeatLog.getLastHeartbeatTime() == null
					|| monitoringAppHeartbeatLog.getLastHeartbeatTime().getTime() != lastHeartbeatTime.getTime()) {
				monitoringAppHeartbeatLog.setLastHeartbeatTime(lastHeartbeatTime);
				monitoringAppHeartbeatLogRepo.save(monitoringAppHeartbeatLog);
			}
		}
	}
}
