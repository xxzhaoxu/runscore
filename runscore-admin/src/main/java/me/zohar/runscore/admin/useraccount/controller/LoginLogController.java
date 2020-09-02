package me.zohar.runscore.admin.useraccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.useraccount.param.LoginLogQueryCondParam;
import me.zohar.runscore.useraccount.service.LoginLogService;

@Controller
@RequestMapping("/loginLog")
public class LoginLogController {

	@Autowired
	private LoginLogService loginLogService;

	@GetMapping("/findLoginLogByPage")
	@ResponseBody
	public Result findLoginLogByPage(LoginLogQueryCondParam param) {
		return Result.success().setData(loginLogService.findLoginLogByPage(param));
	}

	@GetMapping("/findOnlineAccountByPage")
	@ResponseBody
	public Result findOnlineAccountByPage(LoginLogQueryCondParam param) {
		return Result.success().setData(loginLogService.findOnlineAccountByPage(param));
	}

	@GetMapping("/logout")
	@ResponseBody
	public Result logout(String sessionId) {
		loginLogService.logout(sessionId);
		return Result.success();
	}

}
