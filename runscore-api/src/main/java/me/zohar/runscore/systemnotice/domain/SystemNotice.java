package me.zohar.runscore.systemnotice.domain;

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
@Table(name = "system_notice")
@DynamicInsert(true)
@DynamicUpdate(true)
public class SystemNotice implements Serializable {

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

	private String title;

	@Column(name = "content", length = 2000)
	private String content;

	private Date createTime;

	private Date publishTime;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

}
