package me.zohar.runscore.role.repo;

import java.util.List;

import me.zohar.runscore.role.domain.JbPermission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * <p>
  * 权限表 Mapper 接口
 * </p>
 *
 * @author LIUYILIN
 * @since 2019-04-11
 */
public interface JbPermissionRepo extends JpaRepository<JbPermission, String>, JpaSpecificationExecutor<JbPermission> {
	List<JbPermission> findByPid(String pid);
}