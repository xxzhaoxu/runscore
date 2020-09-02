package me.zohar.runscore.admin.systemnotice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.common.operlog.OperLog;
import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.systemnotice.param.AddOrUpdateSystemNoticeParam;
import me.zohar.runscore.systemnotice.param.SystemNoticeQueryCondParam;
import me.zohar.runscore.systemnotice.service.SystemNoticeService;

@Controller
@RequestMapping("/systemNotice")
public class SystemNoticeController {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private SystemNoticeService systemNoticeService;

	@GetMapping("/findSystemNoticeById")
	@ResponseBody
	public Result findSystemNoticeById(String id) {
		return Result.success().setData(systemNoticeService.findSystemNoticeById(id));
	}

	@OperLog(system = "后台管理", module = "系统公告", operate = "删除系统公告")
	@GetMapping("/delSystemNoticeById")
	@ResponseBody
	public Result delSystemNoticeById(String id) {
		demoMode();
		systemNoticeService.delSystemNoticeById(id);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "系统公告", operate = "添加或者编辑系统公告")
	@PostMapping("/addOrUpdateSystemNotice")
	@ResponseBody
	public Result addOrUpdateSystemNotice(AddOrUpdateSystemNoticeParam param) {
		demoMode();
		systemNoticeService.addOrUpdateSystemNotice(param);
		return Result.success();
	}

	@GetMapping("/findSystemNoticeByPage")
	@ResponseBody
	public Result findSystemNoticeByPage(SystemNoticeQueryCondParam param) {
		return Result.success().setData(systemNoticeService.findSystemNoticeByPage(param));
	}

	//演示模式
	public void demoMode(){
		String demoMode = redisTemplate.opsForValue().get("demoMode");
		if("1".equals(demoMode)){
			throw new BizException(BizError.演示模式无法执行该操作);
		}
	}
}
