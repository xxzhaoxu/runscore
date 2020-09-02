package me.zohar.runscore.merchant.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.runscore.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class MerchantSettlementRecordQueryCondParam extends PageParam {

	private String orderNo;

	private String merchantId;

	private String state;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date applyStartTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date applyEndTime;

}
