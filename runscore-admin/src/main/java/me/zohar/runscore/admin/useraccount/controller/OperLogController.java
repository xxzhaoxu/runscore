package me.zohar.runscore.admin.useraccount.controller;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.useraccount.param.OperLogQueryCondParam;
import me.zohar.runscore.useraccount.service.OperLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/operLog")
public class OperLogController {

	@Autowired
	private OperLogService operLogService;

	@GetMapping("/findOperLogByPage")
	@ResponseBody
	public Result findOperLogByPage(OperLogQueryCondParam param) {
		return Result.success().setData(operLogService.findOperLogByPage(param));
	}

}
