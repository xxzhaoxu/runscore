package me.zohar.runscore.dictconfig.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "config_item")
@DynamicInsert(true)
@DynamicUpdate(true)
@ToString
public class ConfigItem implements Serializable {

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
	 * 配置项code
	 */
	private String configCode;

	/**
	 * 配置项名称
	 */
	private String configName;
	
	/**
	 * 配置项值
	 */
	private String configValue;
	
	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

}
