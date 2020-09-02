package me.zohar.runscore.admin.dataclean.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.common.operlog.OperLog;
import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.dataclean.param.DataCleanParam;
import me.zohar.runscore.dataclean.service.DataCleanService;

@Controller
@RequestMapping("/dataClean")
public class DataCleanController {

	@Autowired
	private DataCleanService dataCleanService;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@OperLog(system = "后台管理", module = "数据清理", operate = "数据清理")
	@PostMapping("/dataClean")
	@ResponseBody
	public Result clean(@RequestBody DataCleanParam param) {
		demoMode();
		dataCleanService.dataClean(param);
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
