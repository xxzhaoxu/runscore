package me.zohar.runscore.role.repo;

import me.zohar.runscore.role.domain.JbRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * <p>
  * 角色表 Mapper 接口
 * </p>
 *
 * @author LIUYILIN
 * @since 2019-04-11
 */
public interface JbRoleRepo extends JpaRepository<JbRole, String>, JpaSpecificationExecutor<JbRole> {

}