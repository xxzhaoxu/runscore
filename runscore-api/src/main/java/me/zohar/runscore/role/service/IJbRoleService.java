package me.zohar.runscore.role.service;

import java.util.List;

import javax.validation.constraints.NotBlank;

import me.zohar.runscore.common.valid.ParamValid;
import me.zohar.runscore.role.domain.JbRole;
import me.zohar.runscore.role.domain.JbRolePermission;
import me.zohar.runscore.role.param.AddOrUpdateRoleParam;
import me.zohar.runscore.role.repo.JbRolePermissionRepo;
import me.zohar.runscore.role.repo.JbRoleRepo;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import cn.hutool.core.util.StrUtil;


/**
 *
 * Role 表数据服务层接口
 *
 */
@Validated
@Service
public class IJbRoleService {

	@Autowired
	private JbRoleRepo roleRepo;

	@Autowired
	private JbRolePermissionRepo rolePermissionRepo;

	@Transactional(readOnly = true)
	public JbRole findJbRoleById(String id) {
		return roleRepo.getOne(id);
	}

	@ParamValid
	@Transactional
	public void addOrUpdateRole(AddOrUpdateRoleParam configParam) {
		// 新增
		if (StrUtil.isBlank(configParam.getId())) {
			JbRole configItem = configParam.convertToPo();
			roleRepo.save(configItem);
		}
		// 修改
		else {
			JbRole configItem = roleRepo.getOne(configParam.getId());
			BeanUtils.copyProperties(configParam, configItem);
			roleRepo.save(configItem);
		}
	}

	@Transactional
	public void delRoleById(@NotBlank String id) {
		//查询出本角色已经分配了的权限
		List<JbRolePermission> roleRightList = rolePermissionRepo.findByRid(id);
		//如果存在权限，先进行删除
		if (roleRightList.size() > 0) {
			rolePermissionRepo.deleteAll(roleRightList);
		}
		roleRepo.deleteById(id);
	}

	public List<JbRole> findAll() {
		return roleRepo.findAll();
	}



}
