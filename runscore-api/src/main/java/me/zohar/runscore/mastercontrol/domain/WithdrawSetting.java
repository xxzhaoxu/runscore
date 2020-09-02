package me.zohar.runscore.mastercontrol.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zohar.runscore.common.utils.IdUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "withdraw_setting")
@DynamicInsert(true)
@DynamicUpdate(true)
public class WithdrawSetting implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 每日提现次数上限
	 */
	private Integer everydayWithdrawTimesUpperLimit;

	private Double withdrawLowerLimit;

	private Double withdrawUpperLimit;

	private String withdrawExplain;

	/**
	 * 最近修改时间
	 */
	private Date latelyUpdateTime;

	public static WithdrawSetting build() {
		WithdrawSetting setting = new WithdrawSetting();
		setting.setId(IdUtils.getId());
		return setting;
	}

}
