package me.zohar.runscore.merchant.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.dictconfig.DictHolder;
import me.zohar.runscore.merchant.domain.MerchantSettlementRecord;

@Data
public class MerchantSettlementRecordVO {

	private String id;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 提现金额
	 */
	private Double withdrawAmount;

	/**
	 * 申请时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date applyTime;

	/**
	 * 状态
	 */
	private String state;
	
	private String stateName;

	/**
	 * 备注
	 */
	private String note;

	/**
	 * 审核时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date approvalTime;

	/**
	 * 确认到帐时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date confirmCreditedTime;

	/**
	 * 接入商户名称
	 */
	private String merchantName;

	/**
	 * 开户银行
	 */
	private String openAccountBank;

	/**
	 * 开户人姓名
	 */
	private String accountHolder;

	/**
	 * 银行卡账号
	 */
	private String bankCardAccount;

	public static List<MerchantSettlementRecordVO> convertFor(
			List<MerchantSettlementRecord> merchantSettlementRecords) {
		if (CollectionUtil.isEmpty(merchantSettlementRecords)) {
			return new ArrayList<>();
		}
		List<MerchantSettlementRecordVO> vos = new ArrayList<>();
		for (MerchantSettlementRecord merchantSettlementRecord : merchantSettlementRecords) {
			vos.add(convertFor(merchantSettlementRecord));
		}
		return vos;
	}

	public static MerchantSettlementRecordVO convertFor(MerchantSettlementRecord merchantSettlementRecord) {
		if (merchantSettlementRecord == null) {
			return null;
		}
		MerchantSettlementRecordVO vo = new MerchantSettlementRecordVO();
		BeanUtils.copyProperties(merchantSettlementRecord, vo);
		vo.setStateName(DictHolder.getDictItemName("merchantSettlementState", vo.getState()));
		if (merchantSettlementRecord.getMerchant() != null) {
			vo.setMerchantName(merchantSettlementRecord.getMerchant().getMerchantName());
		}
		if (merchantSettlementRecord.getMerchantBankCard() != null) {
			vo.setOpenAccountBank(merchantSettlementRecord.getMerchantBankCard().getOpenAccountBank());
			vo.setAccountHolder(merchantSettlementRecord.getMerchantBankCard().getAccountHolder());
			vo.setBankCardAccount(merchantSettlementRecord.getMerchantBankCard().getBankCardAccount());
		}
		return vo;
	}

}
