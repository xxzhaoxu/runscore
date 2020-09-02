package me.zohar.runscore.useraccount.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "oper_log")
@DynamicInsert(true)
@DynamicUpdate(true)
public class OperLog implements Serializable {

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

	private String system;

	/**
	 * 模块
	 */
	private String module;

	/**
	 * 操作类型
	 */
	private String operate;

	/**
	 * 请求方式:get/post
	 */
	private String requestMethod;

	/**
	 *
	 */
	private String requestUrl;

	private String requestParam;

	private String ipAddr;

	private String operAccountId;

	private String operName;

	private Date operTime;

}
