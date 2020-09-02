package me.zohar.runscore.role.repo;

import java.util.List;

import me.zohar.runscore.role.domain.JbRolePermission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
  * 角色权限表 Mapper 接口
 * </p>
 *
 * @author LIUYILIN
 * @since 2019-04-11
 */
public interface JbRolePermissionRepo extends JpaRepository<JbRolePermission, String>, JpaSpecificationExecutor<JbRolePermission> {
	/**
	 * 根据角色ID获取对应的所有关联权限的ID
	 * @param id 角色Id
	 * @return
	 */
	List<JbRolePermission> findByRid(String id);
}