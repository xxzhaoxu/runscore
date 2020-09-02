package me.zohar.runscore.statisticalanalysis.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import me.zohar.runscore.statisticalanalysis.domain.TotalAccountReceiveResultsSituation;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;

@Data
public class AccountReceiveResultsSituationVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String inviterId;

	private String accountType;

	private Boolean deletedFlag;

	private String userName;

	private Double totalTradeAmount;

	private Long totalPaidOrderNum;

	private Long totalOrderNum;

	private Double yesterdayTradeAmount;

	private Long yesterdayPaidOrderNum;

	private Long yesterdayOrderNum;

	private Double yesterdaySuccessRate;

	private Double todayTradeAmount;

	private Long todayPaidOrderNum;

	private Long todayOrderNum;

	private Double todaySuccessRate;


	public static List<AccountReceiveResultsSituationVO> convertForTotal(List<TotalAccountReceiveResultsSituation> situations) {
		if (CollectionUtil.isEmpty(situations)) {
			return new ArrayList<>();
		}

		List<AccountReceiveResultsSituationVO> vos = new ArrayList<>();
		for (TotalAccountReceiveResultsSituation situation : situations) {
			vos.add(convertForTotal(situation));
		}
		return vos;
	}

	public static AccountReceiveResultsSituationVO convertForTotal(TotalAccountReceiveResultsSituation situation) {
		if (situation == null) {
			return null;
		}
		AccountReceiveResultsSituationVO vo = new AccountReceiveResultsSituationVO();
		BeanUtils.copyProperties(situation, vo);
		return vo;
	}



}
