package me.zohar.runscore.gatheringcode.controller;

import java.util.List;

import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.gatheringcode.param.GatheringCodeParam;
import me.zohar.runscore.gatheringcode.param.GatheringCodeQueryCondParam;
import me.zohar.runscore.gatheringcode.service.GatheringCodeService;
import me.zohar.runscore.useraccount.vo.UserAccountDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/gatheringCode")
public class GatheringCodeController {

	@Autowired
	private GatheringCodeService gatheringCodeService;

	@PostMapping("/switchGatheringCode")
	@ResponseBody
	public Result switchGatheringCode(@RequestBody List<String> gatheringCodeIds) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		gatheringCodeService.switchGatheringCode(gatheringCodeIds, user.getUserAccountId());
		return Result.success();
	}

	@GetMapping("/delMyGatheringCodeById")
	@ResponseBody
	public Result delMyGatheringCodeById(String id) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		gatheringCodeService.delMyGatheringCodeById(id, user.getUserAccountId());
		return Result.success();
	}

	@PostMapping("/addGatheringCode")
	@ResponseBody
	public Result addGatheringCode(@RequestBody GatheringCodeParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		gatheringCodeService.addGatheringCode(param, user.getUserAccountId());
		return Result.success();
	}

	@GetMapping("/findMyGatheringCodeByPage")
	@ResponseBody
	public Result findMyGatheringCodeByPage(GatheringCodeQueryCondParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		return Result.success().setData(gatheringCodeService.findMyGatheringCodeUsageByPage(param));
	}

	@GetMapping("/findAllGatheringCode")
	@ResponseBody
	public Result findAllGatheringCode() {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(gatheringCodeService.findAllGatheringCode(user.getUserAccountId()));
	}

	@GetMapping("/updateInUseFlag")
	@ResponseBody
	public Result updateInUseFlag(String id, Boolean inUse) {
		gatheringCodeService.updateInUseFlag(id, inUse);
		return Result.success();
	}

}
