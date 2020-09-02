package me.zohar.runscore.useraccount.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import me.zohar.runscore.common.vo.PageResult;
import me.zohar.runscore.useraccount.domain.OperLog;
import me.zohar.runscore.useraccount.param.OperLogQueryCondParam;
import me.zohar.runscore.useraccount.repo.OperLogRepo;
import me.zohar.runscore.useraccount.vo.OperLogVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;








import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

@Validated
@Service
public class OperLogService {

	@Autowired
	private OperLogRepo operLogRepo;

	@Transactional(readOnly = true)
	public PageResult<OperLogVO> findOperLogByPage(OperLogQueryCondParam param) {
		Specification<OperLog> spec = new Specification<OperLog>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<OperLog> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getIpAddr())) {
					predicates.add(builder.equal(root.get("ipAddr"), param.getIpAddr()));
				}
				if (StrUtil.isNotEmpty(param.getUserName())) {
					predicates.add(builder.equal(root.get("userName"), param.getUserName()));
				}
				if (param.getStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("operTime").as(Date.class),
							DateUtil.beginOfDay(param.getStartTime())));
				}
				if (param.getEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("operTime").as(Date.class),
							DateUtil.endOfDay(param.getEndTime())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<OperLog> result = operLogRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("operTime"))));
		PageResult<OperLogVO> pageResult = new PageResult<>(OperLogVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void recordOperLog(OperLog operLog) {
		operLogRepo.save(operLog);
	}

}
