package me.zohar.runscore.dictconfig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.dictconfig.DictHolder;
import me.zohar.runscore.dictconfig.service.ConfigService;

@Controller
@RequestMapping("/dictconfig")
public class DictConfigController {

	@Autowired
	private ConfigService configService;

	@GetMapping("/findDictItemInCache")
	@ResponseBody
	public Result findDictItemInCache(String dictTypeCode) {
		return Result.success().setData(DictHolder.findDictItem(dictTypeCode));
	}

	@GetMapping("/findConfigItemByConfigCode")
	@ResponseBody
	public Result findConfigItemByConfigCode(String configCode) {
		return Result.success().setData(configService.findConfigItemByConfigCode(configCode));
	}
}
