package me.zohar.runscore.merchant.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.merchant.domain.MerchantBankCard;

@Data
public class AddOrUpdateMerchantBankCardParam {

	private String id;

	/**
	 * 开户银行
	 */
	@NotBlank
	private String openAccountBank;

	/**
	 * 开户人姓名
	 */
	@NotBlank
	private String accountHolder;

	/**
	 * 银行卡账号
	 */
	@NotBlank
	private String bankCardAccount;

	public MerchantBankCard convertToPo(String merchantId) {
		MerchantBankCard po = new MerchantBankCard();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setDeletedFlag(false);
		po.setCreateTime(new Date());
		po.setBankInfoLatelyModifyTime(po.getCreateTime());
		po.setMerchantId(merchantId);
		return po;
	}

}
