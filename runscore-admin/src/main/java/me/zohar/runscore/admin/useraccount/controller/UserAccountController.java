package me.zohar.runscore.admin.useraccount.controller;

import me.zohar.runscore.common.auth.GoogleAuthenticator;
import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.common.operlog.OperLog;
import me.zohar.runscore.common.vo.Result;
import me.zohar.runscore.useraccount.param.AccountChangeLogQueryCondParam;
import me.zohar.runscore.useraccount.param.AddUserAccountParam;
import me.zohar.runscore.useraccount.param.AdjustCashDepositParam;
import me.zohar.runscore.useraccount.param.AdjustInviteCodeQuotaParam;
import me.zohar.runscore.useraccount.param.SaveAccountReceiveOrderChannelParam;
import me.zohar.runscore.useraccount.param.UpdateLowerLevelAccountStateParam;
import me.zohar.runscore.useraccount.param.UserAccountEditParam;
import me.zohar.runscore.useraccount.param.UserAccountQueryCondParam;
import me.zohar.runscore.useraccount.service.AccountChannelRebateService;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/userAccount")
public class UserAccountController {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private AccountChannelRebateService accountChannelRebateService;

	@OperLog(system = "后台管理", module = "账号管理", operate = "配置接单通道")
	@PostMapping("/saveAccountReceiveOrderChannel")
	@ResponseBody
	public Result saveAccountReceiveOrderChannel(@RequestBody SaveAccountReceiveOrderChannelParam param) {
		accountChannelRebateService.saveAccountReceiveOrderChannel(param);
		return Result.success();
	}

	@PostMapping("/bindGoogleAuth")
	@ResponseBody
	public Result bindGoogleAuth(String userAccountId, String googleSecretKey, String googleVerCode) {
		userAccountService.bindGoogleAuth(userAccountId, googleSecretKey, googleVerCode);
		return Result.success();
	}

	@GetMapping("/generateGoogleSecretKey")
	@ResponseBody
	public Result generateGoogleSecretKey() {
		return Result.success().setData(GoogleAuthenticator.generateSecretKey());
	}

	@GetMapping("/getGoogleAuthInfo")
	@ResponseBody
	public Result getGoogleAuthInfo(String userAccountId) {
		return Result.success().setData(userAccountService.getGoogleAuthInfo(userAccountId));
	}

	@GetMapping("/findAccountReceiveOrderChannelByAccountId")
	@ResponseBody
	public Result findAccountReceiveOrderChannelByAccountId(String accountId) {
		return Result.success()
				.setData(accountChannelRebateService.findAccountReceiveOrderChannelByAccountId(accountId));
	}

	@GetMapping("/findAccountChangeLogByPage")
	@ResponseBody
	public Result findAccountChangeLogByPage(AccountChangeLogQueryCondParam param) {
		return Result.success().setData(userAccountService.findAccountChangeLogByPage(param));
	}

	@GetMapping("/findUserAccountDetailsInfoById")
	@ResponseBody
	public Result findUserAccountDetailsInfoById(String userAccountId) {
		return Result.success().setData(userAccountService.findUserAccountDetailsInfoById(userAccountId));
	}

	@GetMapping("/findUserAccountDetailsInfoByPage")
	@ResponseBody
	public Result findUserAccountDetailsInfoByPage(UserAccountQueryCondParam param) {
		return Result.success().setData(userAccountService.findUserAccountDetailsInfoByPage(param));
	}

	//查询会员账号
	@GetMapping("/findUserAccountDetailsInfoMemberByPage")
	@ResponseBody
	public Result findUserAccountDetailsInfoMemberByPage(UserAccountQueryCondParam param) {
		param.setAccountType("member");
		return Result.success().setData(userAccountService.findUserAccountDetailsInfoByPage(param));
	}

	//查询信用会员账号
	@GetMapping("/findUserAccountDetailsInfoCreditMemberByPage")
	@ResponseBody
	public Result findUserAccountDetailsInfoCreditMemberByPage(UserAccountQueryCondParam param) {
		param.setAccountType("creditMember");
		return Result.success().setData(userAccountService.findUserAccountDetailsInfoByPage(param));
	}

	//查询代理账号
	@GetMapping("/findUserAccountDetailsInfoAgentByPage")
	@ResponseBody
	public Result findUserAccountDetailsInfoAgentByPage(UserAccountQueryCondParam param) {
		param.setAccountType("agent");
		return Result.success().setData(userAccountService.findUserAccountDetailsInfoByPage(param));
	}

	//查询后台账号
	@GetMapping("/findUserAccountDetailsInfoBackgroundAccountByPage")
	@ResponseBody
	public Result findUserAccountDetailsInfoBackgroundAccountByPage(UserAccountQueryCondParam param) {
		param.setAccountType("admin");
		return Result.success().setData(userAccountService.findUserAccountDetailsInfoByPage(param));
	}



	@GetMapping("/findAllLowerLevelAccount")
	@ResponseBody
	public Result findAllLowerLevelAccount(String userAccountId) {
		return Result.success().setData(userAccountService.findAllLowerLevelAccount(userAccountId));
	}


	@OperLog(system = "后台管理", module = "账号管理", operate = "退出排队队列")
	@GetMapping("/updateReceiveOrderStateToStop")
	@ResponseBody
	public Result updateReceiveOrderStateToStop(String userAccountId) {
		demoMode();
		//把会员接单状态改为停止接单
		userAccountService.updateReceiveOrderState(userAccountId, "2");
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "修改登陆密码")
	@PostMapping("/modifyLoginPwd")
	@ResponseBody
	public Result modifyLoginPwd(String userAccountId, String newLoginPwd) {
		demoMode();
		userAccountService.modifyLoginPwd(userAccountId, newLoginPwd);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "修改二级密码")
	@PostMapping("/modifyMoneyPwd")
	@ResponseBody
	public Result modifyMoneyPwd(String userAccountId, String newMoneyPwd) {
		demoMode();
		userAccountService.modifyMoneyPwd(userAccountId, newMoneyPwd);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "修改账号")
	@PostMapping("/updateUserAccount")
	@ResponseBody
	public Result updateUserAccount(UserAccountEditParam param) {
		demoMode();
		userAccountService.updateUserAccount(param);
		return Result.success();
	}

	@GetMapping("/getUserAccountInfo")
	@ResponseBody
	public Result getUserAccountInfo() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if ("anonymousUser".equals(principal)) {
			return Result.success();
		}
		UserAccountDetails user = (UserAccountDetails) principal;
		UserAccountInfoVO userAccountInfo = userAccountService.getUserAccountInfo(user.getUserAccountId());
		return Result.success().setData(userAccountInfo);
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "删除账号")
	@GetMapping("/delUserAccount")
	@ResponseBody
	public Result delUserAccount(String userAccountId) {
		demoMode();
		userAccountService.delUserAccount(userAccountId);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "添加账号")
	@PostMapping("/addUserAccount")
	@ResponseBody
	public Result addUserAccount(AddUserAccountParam param) {
		userAccountService.addUserAccount(param);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "调整保证金")
	@PostMapping("/adjustCashDeposit")
	@ResponseBody
	public Result adjustCashDeposit(AdjustCashDepositParam param) {
		userAccountService.adjustCashDeposit(param);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "调整邀请码配额")
	@PostMapping("/adjustInviteCodeQuota")
	@ResponseBody
	public Result adjustInviteCodeQuota(AdjustInviteCodeQuotaParam param) {
		userAccountService.adjustInviteCodeQuota(param);
		return Result.success();
	}

	@OperLog(system = "后台管理", module = "账号管理", operate = "修改下级账号状态")
	@PostMapping("/updateLowerLevelAccountState")
	@ResponseBody
	public Result updateLowerLevelAccountState(UpdateLowerLevelAccountStateParam param) {
		userAccountService.updateLowerLevelAccountState(param);
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
