package me.zohar.runscore.admin.dictconfig.controller;

import me.zohar.runscore.dictconfig.vo.ConfigItemVO;
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
import me.zohar.runscore.dictconfig.DictHolder;
import me.zohar.runscore.dictconfig.param.AddOrUpdateDictTypeParam;
import me.zohar.runscore.dictconfig.param.ConfigItemQueryCondParam;
import me.zohar.runscore.dictconfig.param.ConfigParam;
import me.zohar.runscore.dictconfig.param.DictTypeQueryCondParam;
import me.zohar.runscore.dictconfig.param.UpdateDictDataParam;
import me.zohar.runscore.dictconfig.service.ConfigService;
import me.zohar.runscore.dictconfig.service.DictService;

@Controller
@RequestMapping("/dictconfig")
public class DictConfigController {

	@Autowired
	private ConfigService configService;

	@Autowired
	private DictService dictService;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@OperLog(system = "后台管理", module = "字典配置项", operate = "更新字典项")
	@PostMapping("/updateDictData")
	@ResponseBody
	public Result updateDictData(@RequestBody UpdateDictDataParam param) {
		demoMode();
		dictService.updateDictData(param);
		return Result.success();
	}

	@GetMapping("/findDictItemByDictTypeId")
	@ResponseBody
	public Result findDictItemByDictTypeId(String dictTypeId) {
		return Result.success().setData(dictService.findDictItemByDictTypeId(dictTypeId));
	}

	@OperLog(system = "后台管理", module = "字典配置项", operate = "删除字典类型")
	@GetMapping("/delDictTypeById")
	@ResponseBody
	public Result delDictTypeById(String id) {
		demoMode();
		dictService.delDictTypeById(id);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "字典配置项", operate = "添加或修改字典类型")
	@PostMapping("/addOrUpdateDictType")
	@ResponseBody
	public Result addOrUpdateDictType(@RequestBody AddOrUpdateDictTypeParam param) {
		demoMode();
		dictService.addOrUpdateDictType(param);
		return Result.success();
	}

	@GetMapping("/findDictTypeById")
	@ResponseBody
	public Result findDictTypeById(String id) {
		return Result.success().setData(dictService.findDictTypeById(id));
	}

	@GetMapping("/findDictTypeByPage")
	@ResponseBody
	public Result findDictTypeByPage(DictTypeQueryCondParam param) {
		return Result.success().setData(dictService.findDictTypeByPage(param));
	}

	@GetMapping("/findDictItemInCache")
	@ResponseBody
	public Result findDictItemInCache(String dictTypeCode) {
		return Result.success().setData(DictHolder.findDictItem(dictTypeCode));
	}

	@GetMapping("/findConfigItemByPage")
	@ResponseBody
	public Result findConfigItemByPage(ConfigItemQueryCondParam param) {
		return Result.success().setData(configService.findConfigItemByPage(param));
	}

	@GetMapping("/findConfigItemById")
	@ResponseBody
	public Result findConfigItemById(String id) {
		return Result.success().setData(configService.findConfigItemById(id));
	}

	@GetMapping("/findConfigItemByConfigCode")
	@ResponseBody
	public Result findConfigItemByConfigCode(String configCode) {

		ConfigItemVO configItemVO = new ConfigItemVO();
		try {
			configItemVO = configService.findConfigItemByConfigCode(configCode);
		}catch (Exception e){
			e.printStackTrace();
		}

		return Result.success().setData(configItemVO);
	}

	@OperLog(system = "后台管理", module = "字典配置项", operate = "添加或修改配置项")
	@PostMapping("/addOrUpdateConfig")
	@ResponseBody
	public Result addOrUpdateConfig(@RequestBody ConfigParam configParam) {
		demoMode();
		configService.addOrUpdateConfig(configParam);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "字典配置项", operate = "删除配置项")
	@GetMapping("/delConfigById")
	@ResponseBody
	public Result delConfigById(String id) {
		demoMode();
		configService.delConfigById(id);
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
