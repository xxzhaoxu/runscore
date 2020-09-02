package me.zohar.runscore.gatheringcode.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import me.zohar.runscore.dictconfig.DictHolder;
import me.zohar.runscore.gatheringcode.domain.GatheringCode;
import me.zohar.runscore.gatheringcode.domain.GatheringCodeUsage;

@Data
public class GatheringCodeUsageVO implements Serializable {

	/**
	*
	*/
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	private String id;

	private String gatheringChannelId;

	private String gatheringChannelName;

	private String gatheringChannelCode;

	/**
	 * 状态,启用:1;禁用:0
	 */
	private String state;

	private String stateName;

	private Boolean fixedGatheringAmount;

	/**
	 * 收款金额
	 */
	private Double gatheringAmount;

	/**
	 * 收款人
	 */
	private String payee;

	/**
	 * 支付宝userid
	 */
	private String alipayUserid;

	private String mobile;

	private String realName;

	private String account;

	/**
	 * 银行
	 */
	private String openAccountBank;

	/**
	 * 银行编号大写
	 */
	private String bankShortName;

	/**
	 * 开户人
	 */
	private String accountHolder;

	/**
	 * 卡号
	 */
	private String bankCardAccount;

	/**
	 * 使用中
	 */
	private Boolean inUse;

	/**
	 * 发起审核时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date initiateAuditTime;

	/**
	 * 审核类型
	 */
	private String auditType;

	private String auditTypeName;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private String storageId;

	private String userName;

	private String inviterUserName;

	private Double totalTradeAmount;

	private Long totalPaidOrderNum;

	private Long totalOrderNum;

	private Double totalSuccessRate;

	private Double todayTradeAmount;

	private Long todayPaidOrderNum;

	private Long todayOrderNum;

	private Double todaySuccessRate;

	public static List<GatheringCodeUsageVO> convertFor(Collection<GatheringCodeUsage> gatheringCodeUsages) {
		if (CollectionUtil.isEmpty(gatheringCodeUsages)) {
			return new ArrayList<>();
		}
		List<GatheringCodeUsageVO> vos = new ArrayList<>();
		for (GatheringCodeUsage gatheringCodeUsage : gatheringCodeUsages) {
			vos.add(convertFor(gatheringCodeUsage));
		}
		return vos;
	}

	public static GatheringCodeUsageVO convertFor(GatheringCodeUsage gatheringCodeUsage) {
		if (gatheringCodeUsage == null) {
			return null;
		}
		GatheringCodeUsageVO vo = new GatheringCodeUsageVO();
		BeanUtils.copyProperties(gatheringCodeUsage, vo);
		vo.setStateName(DictHolder.getDictItemName("gatheringCodeState", vo.getState()));
		if (StrUtil.isNotBlank(vo.getAuditType())) {
			vo.setAuditTypeName(DictHolder.getDictItemName("gatheringCodeAuditType", vo.getAuditType()));
		}
		if (gatheringCodeUsage.getUserAccount() != null) {
			vo.setUserName(gatheringCodeUsage.getUserAccount().getUserName());
			if (gatheringCodeUsage.getUserAccount().getInviter() != null) {
				vo.setInviterUserName(gatheringCodeUsage.getUserAccount().getInviter().getUserName());
			}
		}
		if (gatheringCodeUsage.getGatheringChannel() != null) {
			vo.setGatheringChannelName(gatheringCodeUsage.getGatheringChannel().getChannelName());
			vo.setGatheringChannelCode(gatheringCodeUsage.getGatheringChannel().getChannelCode());
		}
		return vo;
	}

	public static List<GatheringCodeUsageVO> convertForGatheringCode(Collection<GatheringCode> gatheringCodes) {
		if (CollectionUtil.isEmpty(gatheringCodes)) {
			return new ArrayList<>();
		}
		List<GatheringCodeUsageVO> vos = new ArrayList<>();
		for (GatheringCode gatheringCode : gatheringCodes) {
			vos.add(convertForGatheringCode(gatheringCode));
		}
		return vos;
	}

	public static GatheringCodeUsageVO convertForGatheringCode(GatheringCode gatheringCode) {
		if (gatheringCode == null) {
			return null;
		}
		GatheringCodeUsageVO vo = new GatheringCodeUsageVO();
		BeanUtils.copyProperties(gatheringCode, vo);
		vo.setStateName(DictHolder.getDictItemName("gatheringCodeState", vo.getState()));
		if (StrUtil.isNotBlank(vo.getAuditType())) {
			vo.setAuditTypeName(DictHolder.getDictItemName("gatheringCodeAuditType", vo.getAuditType()));
		}
		if (gatheringCode.getUserAccount() != null) {
			vo.setUserName(gatheringCode.getUserAccount().getUserName());
		}
		if (gatheringCode.getGatheringChannel() != null) {
			vo.setGatheringChannelName(gatheringCode.getGatheringChannel().getChannelName());
			vo.setGatheringChannelCode(gatheringCode.getGatheringChannel().getChannelCode());
		}
		return vo;
	}

}
