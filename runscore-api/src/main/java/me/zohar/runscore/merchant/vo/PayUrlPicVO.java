package me.zohar.runscore.merchant.vo;

import lombok.Data;
import me.zohar.runscore.dictconfig.ConfigHolder;
import me.zohar.runscore.merchant.domain.MerchantOrder;

import org.springframework.beans.BeanUtils;

@Data
public class PayUrlPicVO {

	/**
	 * 订单号
	 */
	private String orderNo;

	private String payUrlPic;

	/**
	 * 银行
	 */
	private String openAccountBank;

	/**
	 * 开户人
	 */
	private String accountHolder;

	/**
	 * 卡号
	 */
	private String bankCardAccount;

	/**
	 * 收款渠道
	 */
	private String gatheringChannelCode;

	private String gatheringChannelName;

	public static PayUrlPicVO convertFor(MerchantOrder merchantOrder) {
		if (merchantOrder == null) {
			return null;
		}
		PayUrlPicVO vo = new PayUrlPicVO();
		BeanUtils.copyProperties(merchantOrder, vo);
		if (merchantOrder.getGatheringChannel() != null) {
			vo.setGatheringChannelCode(merchantOrder.getGatheringChannel().getChannelCode());
			vo.setGatheringChannelName(merchantOrder.getGatheringChannel().getChannelName());
		}
		if (merchantOrder.getGatheringCode() != null) {
			if(merchantOrder.getGatheringCode().getStorageId() != null){
				vo.setPayUrlPic(ConfigHolder.getConfigValue("homePageUrl") + "/storage/fetch/" +merchantOrder.getGatheringCode().getStorageId());
			}
			vo.setBankCardAccount(merchantOrder.getGatheringCode().getBankCardAccount());
			vo.setOpenAccountBank(merchantOrder.getGatheringCode().getOpenAccountBank());
			vo.setAccountHolder(merchantOrder.getGatheringCode().getAccountHolder());
		}
		return vo;
	}

}
