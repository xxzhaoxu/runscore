package me.zohar.runscore.admin.role.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.common.operlog.OperLog;
import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.role.domain.JbPermission;
import me.zohar.runscore.role.domain.JbRole;
import me.zohar.runscore.role.domain.JbRolePermission;
import me.zohar.runscore.role.param.AddOrUpdateRoleParam;
import me.zohar.runscore.role.service.IJbPermissionService;
import me.zohar.runscore.role.service.IJbRolePermissionService;
import me.zohar.runscore.role.service.IJbRoleService;
import me.zohar.runscore.role.vo.MenuVO;
import me.zohar.runscore.useraccount.service.UserAccountService;
import me.zohar.runscore.useraccount.vo.UserAccountDetails;
import me.zohar.runscore.useraccount.vo.UserAccountInfoVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * 角色权限管理相关操作
 */
@Controller
@RequestMapping("/role")
public class RoleController  {

	@Autowired
	private IJbRoleService roleService;

	@Autowired
	private IJbPermissionService permissionService;

	@Autowired
	private IJbRolePermissionService rolePermissionService;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private StringRedisTemplate redisTemplate;


	@ResponseBody
	@RequestMapping("/findRoleList")
	public Result findRoleList() {
		List<JbRole> list = roleService.findAll();
		return Result.success().setData(list,list.size());
	}

	@ResponseBody
	@RequestMapping("/selectMenuVOByUserId")
	public Result selectMenuVOByUserId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(principal)) {
			return Result.success();
		}
		UserAccountDetails user = (UserAccountDetails) principal;
		UserAccountInfoVO userAccountInfo = userAccountService.getUserAccountInfo(user.getUserAccountId());
		List<MenuVO> menuList = permissionService.selectMenuVOByUserId(userAccountInfo.getRoleId());
		return Result.success().setData(menuList);
	}

	@GetMapping("/findJbRoleById")
	@ResponseBody
	public Result findJbRoleById(String id) {
		return Result.success().setData(roleService.findJbRoleById(id));
	}

	@OperLog(system = "后台管理", module = "角色权限管理", operate = "添加或修改角色")
	@PostMapping("/addOrUpdateRole")
	@ResponseBody
	public Result addOrUpdateRole(@RequestBody AddOrUpdateRoleParam configParam) {
		demoMode();
		roleService.addOrUpdateRole(configParam);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "角色权限管理", operate = "删除角色")
	@GetMapping("/delRoleById")
	@ResponseBody
	public Result delRoleById(String id) {
		demoMode();
		roleService.delRoleById(id);
		return Result.success();
	}


	/**
	 * 获取权限树
	 * @param model
	 * @param roleId
	 * @return
	 */
	@RequestMapping("/right")
	@ResponseBody
	public String right(String roleId ) {
		List<JbPermission> list = permissionService.findAll();
		List<JbRolePermission> roleRightList = rolePermissionService.findByRid(roleId);
		List<Map<String,String>> rightList = new ArrayList<Map<String,String>>();
		for(JbPermission r : list){
			Map<String,String> map = new HashMap<String,String>();
			map.put("id", r.getId().toString());
			map.put("pId", r.getPid().toString());
			map.put("name", r.getTitle());
			//默认展开树
			map.put( "open", "true");
			//如果角色已有该权限，则默认选中
			for (JbRolePermission jb : roleRightList) {
				if(jb.getPid().equals(r.getId())){
					map.put( "checked", "true");
					break;
				}
			}
			rightList.add(map);
		}
		return JSONObject.toJSONString(rightList);
	}

	/**
	 * 更新权限列表
	 * @param right
	 * @param page
	 * @param rows
	 * @return
	 */
	@OperLog(system = "后台管理", module = "角色权限管理", operate = "更新权限")
	@RequestMapping("updateRoleRight")
	@ResponseBody
	public Result updateRoleRight(@RequestParam(value="roleId",required = false) String roleId,
			@RequestParam(value="rights",required = false) String rights){
		demoMode();
		rolePermissionService.updateRoleRight(roleId, rights);
		return Result.success();
	}

	//演示模式
	public void demoMode(){
		String demoMode = redisTemplate.opsForValue().get("demoMode");
		if("1".equals(demoMode)){
			throw new BizException(BizError.演示模式无法执行该操作);
		}
	}
}
