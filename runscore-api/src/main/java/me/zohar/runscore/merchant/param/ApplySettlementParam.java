package me.zohar.runscore.merchant.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.merchant.domain.MerchantSettlementRecord;

@Data
public class ApplySettlementParam {

	/**
	 * 提现金额
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double withdrawAmount;

	/**
	 * 资金密码
	 */
	@NotBlank
	private String moneyPwd;

	@NotBlank
	private String merchantId;

	@NotBlank
	private String merchantBankCardId;

	public MerchantSettlementRecord convertToPo() {
		MerchantSettlementRecord po = new MerchantSettlementRecord();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setOrderNo(po.getId());
		po.setApplyTime(new Date());
		po.setState(Constant.商户结算状态_审核中);
		return po;
	}

}
