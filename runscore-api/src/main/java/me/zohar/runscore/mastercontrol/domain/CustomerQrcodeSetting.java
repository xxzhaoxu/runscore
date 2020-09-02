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
@Table(name = "customer_qrcode_setting")
@DynamicInsert(true)
@DynamicUpdate(true)
public class CustomerQrcodeSetting implements Serializable {

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

	private String customerServiceExplain;

	private String customerServiceUrl;

	private String qrcodeStorageId;

	/**
	 * 最近修改时间
	 */
	private Date latelyUpdateTime;

	public static CustomerQrcodeSetting build() {
		CustomerQrcodeSetting setting = new CustomerQrcodeSetting();
		setting.setId(IdUtils.getId());
		return setting;
	}

}
