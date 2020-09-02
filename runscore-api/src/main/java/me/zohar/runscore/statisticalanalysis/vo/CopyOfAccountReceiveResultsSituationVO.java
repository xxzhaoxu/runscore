package me.zohar.runscore.statisticalanalysis.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import me.zohar.runscore.statisticalanalysis.domain.TotalAccountReceiveResultsSituation;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;

@Data
public class CopyOfAccountReceiveResultsSituationVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String receivedAccountId;

	private String userName;

	private String accountType;

	private Double gatheringAmount;

	private Long orderNum;

	private Double paidAmount;

	private Double paidAmount2;

	private Double bounty;

	private Long paidOrderNum;

	private Double rebateAmount;

	private Double successRate;


	public static List<CopyOfAccountReceiveResultsSituationVO> convertForTotal(List<TotalAccountReceiveResultsSituation> situations) {
		if (CollectionUtil.isEmpty(situations)) {
			return new ArrayList<>();
		}

		List<CopyOfAccountReceiveResultsSituationVO> vos = new ArrayList<>();
		for (TotalAccountReceiveResultsSituation situation : situations) {
			vos.add(convertForTotal(situation));
		}
		return vos;
	}

	public static CopyOfAccountReceiveResultsSituationVO convertForTotal(TotalAccountReceiveResultsSituation situation) {
		if (situation == null) {
			return null;
		}
		CopyOfAccountReceiveResultsSituationVO vo = new CopyOfAccountReceiveResultsSituationVO();
		BeanUtils.copyProperties(situation, vo);
		return vo;
	}



}
