package me.zohar.runscore.merchant.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.merchant.domain.Merchant;

@Data
public class MerchantEditParam {

	private String id;

	@NotBlank
	private String userName;

	@NotBlank
	private String merchantNum;

	@NotBlank
	private String merchantName;

	@NotBlank
	private String secretKey;

	private String notifyUrl;

	private String returnUrl;

	@NotBlank
	private String state;

	public Merchant convertToPo() {
		Merchant po = new Merchant();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCreateTime(new Date());
		return po;
	}

}
