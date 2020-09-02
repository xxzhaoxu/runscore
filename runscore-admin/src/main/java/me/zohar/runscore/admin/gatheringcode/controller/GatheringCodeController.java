package me.zohar.runscore.admin.gatheringcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.common.operlog.OperLog;
import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.gatheringcode.param.GatheringCodeParam;
import me.zohar.runscore.gatheringcode.param.GatheringCodeQueryCondParam;
import me.zohar.runscore.gatheringcode.service.GatheringCodeService;

@Controller
@RequestMapping("/gatheringCode")
public class GatheringCodeController {

	@Autowired
	private GatheringCodeService gatheringCodeService;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@OperLog(system = "后台管理", module = "收款方式", operate = "更新收款码使用辨识")
	@GetMapping("/updateInUseFlag")
	@ResponseBody
	public Result updateInUseFlag(String id, Boolean inUse) {
		demoMode();
		gatheringCodeService.updateInUseFlag(id, inUse);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "收款方式", operate = "删除收款方式")
	@GetMapping("/delGatheringCodeById")
	@ResponseBody
	public Result delGatheringCodeById(String id) {
		demoMode();
		gatheringCodeService.delGatheringCodeById(id);
		return Result.success();
	}

	@GetMapping("/findGatheringCodeById")
	@ResponseBody
	public Result findGatheringCodeUsageById(String id) {
		return Result.success().setData(gatheringCodeService.findGatheringCodeUsageById(id));
	}

	@OperLog(system = "后台管理", module = "收款方式", operate = "添加或者修改收款方式")
	@PostMapping("/addOrUpdateGatheringCode")
	@ResponseBody
	public Result addOrUpdateGatheringCode(@RequestBody GatheringCodeParam param) {
		demoMode();
		gatheringCodeService.addOrUpdateGatheringCode(param);
		return Result.success();
	}

	@GetMapping("/updateToNormalState")
	@ResponseBody
	public Result updateToNormalState(String id) {
		demoMode();
		gatheringCodeService.updateToNormalState(id);
		return Result.success();
	}

	@GetMapping("/findGatheringCodeByPage")
	@ResponseBody
	public Result findGatheringCodeByPage(GatheringCodeQueryCondParam param) {
		return Result.success().setData(gatheringCodeService.findGatheringCodeUsageByPage(param));
	}

	//演示模式
	public void demoMode(){
		String demoMode = redisTemplate.opsForValue().get("demoMode");
		if("1".equals(demoMode)){
			throw new BizException(BizError.演示模式无法执行该操作);
		}
	}

}
