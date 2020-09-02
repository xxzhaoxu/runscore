package me.zohar.runscore.merchant.vo;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import me.zohar.runscore.dictconfig.DictHolder;
import me.zohar.runscore.merchant.domain.MerchantOrder;

@Data
public class OrderGatheringCodeVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 收款渠道
	 */
	private String gatheringChannelCode;

	private String gatheringChannelName;

	/**
	 * 收款金额
	 */
	private Double gatheringAmount;

	/**
	 * 订单状态
	 */
	private String orderState;

	private String orderStateName;

	/**
	 * 提交时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date submitTime;

	/**
	 * 有效时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date usefulTime;

	private String gatheringCodeStorageId;

	private String gatheringCodeUrl;

	private String alipayUserid;

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
	 * 二维码内容
	 */
	private String codeContent;

	/**
	 * 同步通知地址
	 */
	private String returnUrl;

	private String mobile;

	private String realName;

	private String account;

	public static OrderGatheringCodeVO convertFor(MerchantOrder merchantOrder) {
		if (merchantOrder == null) {
			return null;
		}
		OrderGatheringCodeVO vo = new OrderGatheringCodeVO();
		BeanUtils.copyProperties(merchantOrder, vo);
		vo.setOrderStateName(DictHolder.getDictItemName("merchantOrderState", vo.getOrderState()));
		vo.setReturnUrl(merchantOrder.getPayInfo().getReturnUrl());
		if (merchantOrder.getGatheringChannel() != null) {
			vo.setGatheringChannelCode(merchantOrder.getGatheringChannel().getChannelCode());
			vo.setGatheringChannelName(merchantOrder.getGatheringChannel().getChannelName());
		}
		if (merchantOrder.getGatheringCode() != null) {
			vo.setAlipayUserid(merchantOrder.getGatheringCode().getAlipayUserid());
			vo.setBankCardAccount(merchantOrder.getGatheringCode().getBankCardAccount());
			vo.setBankShortName(merchantOrder.getGatheringCode().getBankShortName());
			vo.setOpenAccountBank(merchantOrder.getGatheringCode().getOpenAccountBank());
			vo.setAccountHolder(merchantOrder.getGatheringCode().getAccountHolder());
			vo.setCodeContent(merchantOrder.getGatheringCode().getCodeContent());
			vo.setMobile(merchantOrder.getGatheringCode().getMobile());
			vo.setRealName(merchantOrder.getGatheringCode().getRealName());
			vo.setAccount(merchantOrder.getGatheringCode().getAccount());
		}
		return vo;
	}

}
