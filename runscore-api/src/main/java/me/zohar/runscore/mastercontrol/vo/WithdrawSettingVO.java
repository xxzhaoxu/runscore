package me.zohar.runscore.mastercontrol.vo;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import me.zohar.runscore.mastercontrol.domain.WithdrawSetting;

@Data
public class WithdrawSettingVO {

	private String id;

	private Integer everydayWithdrawTimesUpperLimit;

	private Double withdrawLowerLimit;

	private Double withdrawUpperLimit;

	private String withdrawExplain;

	/**
	 * 最近修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date latelyUpdateTime;

	public static WithdrawSettingVO convertFor(WithdrawSetting setting) {
		if (setting == null) {
			return null;
		}
		WithdrawSettingVO vo = new WithdrawSettingVO();
		BeanUtils.copyProperties(setting, vo);
		return vo;
	}

}
