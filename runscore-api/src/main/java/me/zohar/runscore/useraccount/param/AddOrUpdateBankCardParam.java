package me.zohar.runscore.useraccount.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.useraccount.domain.BankCard;

@Data
public class AddOrUpdateBankCardParam {

	private String id;

	/**
	 * 开户银行
	 */
	@NotBlank
	private String openAccountBank;

	/**
	 * 开户人
	 */
	@NotBlank
	private String accountHolder;

	/**
	 * 银行卡号
	 */
	@NotBlank
	private String bankCardAccount;

	public BankCard convertToPo() {
		BankCard po = new BankCard();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCreateTime(new Date());
		po.setLatelyModifyTime(po.getCreateTime());
		po.setDeletedFlag(false);
		return po;
	}

}
