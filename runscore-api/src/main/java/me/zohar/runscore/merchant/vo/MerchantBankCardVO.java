package me.zohar.runscore.merchant.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.merchant.domain.MerchantBankCard;

@Data
public class MerchantBankCardVO {

	private String id;

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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 银行资料最近修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date bankInfoLatelyModifyTime;

	public static List<MerchantBankCardVO> convertFor(List<MerchantBankCard> merchantBankCards) {
		if (CollectionUtil.isEmpty(merchantBankCards)) {
			return new ArrayList<>();
		}
		List<MerchantBankCardVO> vos = new ArrayList<>();
		for (MerchantBankCard merchantBankCard : merchantBankCards) {
			vos.add(convertFor(merchantBankCard));
		}
		return vos;
	}

	public static MerchantBankCardVO convertFor(MerchantBankCard merchantBankCard) {
		if (merchantBankCard == null) {
			return null;
		}
		MerchantBankCardVO vo = new MerchantBankCardVO();
		BeanUtils.copyProperties(merchantBankCard, vo);
		return vo;
	}

}
