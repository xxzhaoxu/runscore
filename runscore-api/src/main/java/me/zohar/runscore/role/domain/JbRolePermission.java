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
 * 角色权限表
 */
@Getter
@Setter
@Entity
@Table(name = "jb_role_permission")
@DynamicInsert(true)
@DynamicUpdate(true)
public class JbRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
	private String id;
    /**
     * 角色ID
     */
	private String rid;
    /**
     * 权限ID
     */
	private String pid;


}
