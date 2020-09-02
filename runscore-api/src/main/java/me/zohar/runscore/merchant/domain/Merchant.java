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
@Table(name = "merchant")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Merchant implements Serializable {

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
	 * 用户名
	 */
	private String userName;

	/**
	 * 商户号
	 */
	private String merchantNum;

	/**
	 * 接入商户名称
	 */
	private String merchantName;

	/**
	 * 密钥
	 */
	private String secretKey;

	private String notifyUrl;

	private String returnUrl;

	/**
	 * 登录密码
	 */
	private String loginPwd;

	/**
	 * 资金密码
	 */
	private String moneyPwd;

	/**
	 * 可提现金额
	 */
	private Double withdrawableAmount;

	/**
	 * 状态
	 */
	private String state;

	private Date createTime;

	/**
	 * 最近登录时间
	 */
	private Date latelyLoginTime;

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
