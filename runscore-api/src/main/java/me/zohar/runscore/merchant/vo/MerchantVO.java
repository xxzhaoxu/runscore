package me.zohar.runscore.merchant.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.dictconfig.DictHolder;
import me.zohar.runscore.merchant.domain.Merchant;

@Data
public class MerchantVO {

	private String id;

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 商户号
	 */
	private String merchantNum;

	/**
	 * 商户名称
	 */
	private String merchantName;

	/**
	 * 密钥
	 */
	private String secretKey;

	private String notifyUrl;

	private String returnUrl;

	private String state;

	private String stateName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 最近登录时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date latelyLoginTime;

	/**
	 * 可提现金额
	 */
	private Double withdrawableAmount;

	public static List<MerchantVO> convertFor(List<Merchant> merchants) {
		if (CollectionUtil.isEmpty(merchants)) {
			return new ArrayList<>();
		}
		List<MerchantVO> vos = new ArrayList<>();
		for (Merchant merchant : merchants) {
			vos.add(convertFor(merchant));
		}
		return vos;
	}

	public static MerchantVO convertFor(Merchant merchant) {
		if (merchant == null) {
			return null;
		}
		MerchantVO vo = new MerchantVO();
		BeanUtils.copyProperties(merchant, vo);
		vo.setStateName(DictHolder.getDictItemName("accountState", vo.getState()));
		return vo;
	}

}
