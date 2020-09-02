package me.zohar.runscore.admin.useraccount.controller;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.useraccount.param.MonitoringAppCollectLogQueryCondParam;
import me.zohar.runscore.useraccount.service.MonitoringAppCollectLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/monitoringAppCollectLog")
public class MonitoringAppCollectLogController {

	@Autowired
	private MonitoringAppCollectLogService monitoringAppCollectLogService;

	@GetMapping("/findMonitoringAppCollectLogByPage")
	@ResponseBody
	public Result findOperLogByPage(MonitoringAppCollectLogQueryCondParam param) {
		return Result.success().setData(monitoringAppCollectLogService.findMonitoringAppCollectLogByPage(param));
	}

}
