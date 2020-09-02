package me.zohar.runscore.merchant.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gathering_channel")
@DynamicInsert(true)
@DynamicUpdate(true)
public class GatheringChannel implements Serializable {

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
	 * 通道code
	 */
	private String channelCode;

	/**
	 * 通道名称
	 */
	private String channelName;
	
	private Double defaultReceiveOrderRabate;

	/**
	 * 是否启用
	 */
	private Boolean enabled;

	private Date createTime;

	/**
	 * 逻辑删除
	 */
	private Boolean deletedFlag;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

}
