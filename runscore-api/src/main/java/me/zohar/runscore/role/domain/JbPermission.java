package me.zohar.runscore.role.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


/**
 * 权限表
 */
@Getter
@Setter
@Entity
@Table(name = "jb_permission")
@DynamicInsert(true)
@DynamicUpdate(true)
public class JbPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
	private String id;
    /**
     * 上级ID
     */
	private String pid;
    /**
     * 标题
     */
	private String title;
    /**
     * 类型 0、菜单 1、功能
     */
	private Integer type;
    /**
     * 状态 0、正常 1、禁用
     */
	private Integer state;
    /**
     * 排序
     */
	private Integer sort;
    /**
     * 地址
     */
	private String url;
    /**
     * 权限编码
     */
	private String permCode;

}
