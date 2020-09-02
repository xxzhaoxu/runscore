package me.zohar.runscore.useraccount.vo;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.useraccount.domain.UserAccount;

@Data
public class CityInfoVO {

	private String id;

	private String province;

	private String city;

	private String cityCode;

	public static CityInfoVO convertFor(UserAccount userAccount) {
		if (userAccount == null) {
			return null;
		}
		CityInfoVO vo = new CityInfoVO();
		BeanUtils.copyProperties(userAccount, vo);
		return vo;
	}

}
