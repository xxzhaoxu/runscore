package me.zohar.runscore.merchant.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.runscore.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class MerchantQueryCondParam extends PageParam {

	/**
	 * 用户名
	 */
	private String userName;

	private String merchantName;

}
