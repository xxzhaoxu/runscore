package me.zohar.runscore.useraccount.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.useraccount.domain.BankCard;

@Data
public class BankCardVO {

	private String id;

	/**
	 * 开户银行
	 */
	private String openAccountBank;

	/**
	 * 开户人
	 */
	private String accountHolder;

	/**
	 * 银行卡号
	 */
	private String bankCardAccount;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 最近修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date latelyModifyTime;

	private String userAccountId;

	public static List<BankCardVO> convertFor(List<BankCard> bankCards) {
		if (CollectionUtil.isEmpty(bankCards)) {
			return new ArrayList<>();
		}
		List<BankCardVO> vos = new ArrayList<>();
		for (BankCard bankCard : bankCards) {
			vos.add(convertFor(bankCard));
		}
		return vos;
	}

	public static BankCardVO convertFor(BankCard bankCard) {
		if (bankCard == null) {
			return null;
		}
		BankCardVO vo = new BankCardVO();
		BeanUtils.copyProperties(bankCard, vo);
		return vo;
	}

}
