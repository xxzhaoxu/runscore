package me.zohar.runscore.merchant.vo;

import lombok.Data;
import me.zohar.runscore.dictconfig.ConfigHolder;

@Data
public class StartOrderSuccessVO {

	private String id;

	/**
	 * 支付地址
	 */
	private String payUrl;

	public static StartOrderSuccessVO convertFor(String id) {
		StartOrderSuccessVO vo = new StartOrderSuccessVO();
		vo.setId(id);
		vo.setPayUrl(ConfigHolder.getConfigValue("merchantOrderPayUrl") + id);
		return vo;
	}

	public static StartOrderSuccessVO convertForPic(String orderNo,String id) {
		StartOrderSuccessVO vo = new StartOrderSuccessVO();
		vo.setId(orderNo);
		vo.setPayUrl(ConfigHolder.getConfigValue("homePageUrl") +"/api/payUrlPic?orderNo=" + id);
		return vo;
	}

}
