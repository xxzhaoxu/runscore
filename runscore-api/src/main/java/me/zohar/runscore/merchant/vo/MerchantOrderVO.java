package me.zohar.runscore.merchant.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import me.zohar.runscore.dictconfig.ConfigHolder;
import me.zohar.runscore.dictconfig.DictHolder;
import me.zohar.runscore.merchant.domain.MerchantOrder;

@Data
public class MerchantOrderVO {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 订单号
	 */
	private String orderNo;

	private String gatheringChannelName;

	/**
	 * 收款金额
	 */
	private Double gatheringAmount;

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

	/**
	 * 订单状态
	 */
	private String orderState;

	private String orderStateName;

	/**
	 * 备注
	 */
	private String note;

	private String platformId;

	private String platformName;

	/**
	 * 接单人账号id
	 */
	private String receivedAccountId;

	/**
	 * 接单人用户名
	 */
	private String receiverUserName;

	/**
	 * 邀请人
	 */
	private String inviterUserName;

	private String payee;

	private String ip;

	private String accountHolder;

	private String mobile;

	/**
	 * 接单时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date receivedTime;

	private String gatheringCodeStorageId;

	/**
	 * 处理时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date dealTime;

	/**
	 * 确认时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date confirmTime;

	/**
	 * 费率
	 */
	private Double rate;

	/**
	 * 返点
	 */
	private Double rebate;

	/**
	 * 奖励金
	 */
	private Double bounty;

	private String payUrl;

	private MerchantOrderPayInfoVO payInfo;

	public static List<MerchantOrderVO> convertFor(List<MerchantOrder> platformOrders) {
		if (CollectionUtil.isEmpty(platformOrders)) {
			return new ArrayList<>();
		}
		List<MerchantOrderVO> vos = new ArrayList<>();
		for (MerchantOrder platformOrder : platformOrders) {
			vos.add(convertFor(platformOrder));
		}
		return vos;
	}

	public static MerchantOrderVO convertFor(MerchantOrder merchantOrder) {
		if (merchantOrder == null) {
			return null;
		}
		MerchantOrderVO vo = new MerchantOrderVO();
		BeanUtils.copyProperties(merchantOrder, vo);
		vo.setGatheringChannelName(merchantOrder.getGatheringChannel().getChannelName());
		vo.setOrderStateName(DictHolder.getDictItemName("merchantOrderState", vo.getOrderState()));
		if (merchantOrder.getMerchant() != null) {
			vo.setPlatformName(merchantOrder.getMerchant().getMerchantName());
		}
		if (StrUtil.isNotBlank(merchantOrder.getReceivedAccountId()) && merchantOrder.getReceivedAccount() != null) {
			vo.setReceiverUserName(merchantOrder.getReceivedAccount().getUserName());
			if(merchantOrder.getReceivedAccount().getInviter() != null){
				vo.setInviterUserName(merchantOrder.getReceivedAccount().getInviter().getUserName());
			}
			vo.setMobile(merchantOrder.getReceivedAccount().getMobile());
			if (merchantOrder.getGatheringCode() != null) {
				vo.setPayee(merchantOrder.getGatheringCode().getPayee());
				vo.setAccountHolder(merchantOrder.getGatheringCode().getAccountHolder());
			}
		}
		if(merchantOrder.getPayInfo() != null){
			vo.setIp(merchantOrder.getPayInfo().getIp());
		}
		vo.setPayUrl(ConfigHolder.getConfigValue("merchantOrderPayUrl") + vo.getOrderNo());
		vo.setPayInfo(MerchantOrderPayInfoVO.convertFor(merchantOrder.getPayInfo()));
		return vo;
	}

}
