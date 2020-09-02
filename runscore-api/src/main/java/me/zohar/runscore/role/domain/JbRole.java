package me.zohar.runscore.role.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * 角色表
 */
@Getter
@Setter
@Entity
@Table(name = "jb_role")
@DynamicInsert(true)
@DynamicUpdate(true)
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class JbRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
	private String id;
    /**
     * 角色
     */
	private String name;
    /**
     * 排序
     */
	private Integer sort;
    /**
     * 描述
     */
	private String description;

}
