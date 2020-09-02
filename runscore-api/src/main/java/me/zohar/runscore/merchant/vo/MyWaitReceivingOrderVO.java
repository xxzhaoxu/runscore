package me.zohar.runscore.merchant.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.merchant.domain.MerchantOrder;

@Data
public class MyWaitReceivingOrderVO {

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
	 * 商户自定义的附加信息
	 */
	private String attch;

	public static List<MyWaitReceivingOrderVO> convertFor(List<MerchantOrder> merchantOrders) {
		if (CollectionUtil.isEmpty(merchantOrders)) {
			return new ArrayList<>();
		}
		List<MyWaitReceivingOrderVO> vos = new ArrayList<>();
		for (MerchantOrder merchantOrder : merchantOrders) {
			vos.add(convertFor(merchantOrder));
		}
		return vos;
	}

	public static MyWaitReceivingOrderVO convertFor(MerchantOrder merchantOrder) {
		if (merchantOrder == null) {
			return null;
		}
		MyWaitReceivingOrderVO vo = new MyWaitReceivingOrderVO();
		BeanUtils.copyProperties(merchantOrder, vo);
		vo.setGatheringChannelName(merchantOrder.getGatheringChannel().getChannelName());
		if (merchantOrder.getPayInfo() != null) {
			vo.setAttch(merchantOrder.getPayInfo().getAttch());
		}
		return vo;
	}

}
