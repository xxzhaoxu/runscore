package me.zohar.runscore.role.service;

import java.util.ArrayList;
import java.util.List;

import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.role.domain.JbRolePermission;
import me.zohar.runscore.role.repo.JbRolePermissionRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;


/**
 * RolePermission 表数据服务层接口
 */
@Validated
@Service
public class IJbRolePermissionService {

	@Autowired
	private JbRolePermissionRepo rolePermissionRepo;

	public boolean existRolePermission(String permissionId) {
		JbRolePermission rp = new JbRolePermission();
		rp.setPid(permissionId);
		//int rlt = rolePermissionRepo.selectCount(new EntityWrapper<JbRolePermission>(rp));
		//return rlt >= 1;
		return true;
	}

	public List<JbRolePermission> findByRid(String id) {
		return rolePermissionRepo.findByRid(id);
	}

	@Transactional
	public void updateRoleRight(String roleId,String rights){
		//查询出本角色已经分配了的权限
		List<JbRolePermission> roleRightList = rolePermissionRepo.findByRid(roleId);
		//如果存在权限，先进行删除
		if (roleRightList.size() > 0) {
			rolePermissionRepo.deleteAll(roleRightList);
		}

		if(rights != null && rights != ""){
			String[] rightIds = rights.split(",");
			if(rightIds != null){
				//添加新分配的权限
				List<JbRolePermission> permissions = new ArrayList<JbRolePermission>();
				JbRolePermission e = null;
				for (String pid : rightIds) {
					e = new JbRolePermission();
					e.setId(IdUtils.getId());
					e.setPid(pid);
					e.setRid(roleId);
					permissions.add(e);
				}
				rolePermissionRepo.saveAll(permissions);
			}
		}

	}

}