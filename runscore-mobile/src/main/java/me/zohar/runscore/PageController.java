package me.zohar.runscore;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

@Controller
public class PageController {

	@GetMapping("/invite-code")
	public String inviteCode() {
		return "invite-code";
	}

	@GetMapping("/virtual-wallet")
	public String virtualWallet() {
		return "virtual-wallet";
	}

	@GetMapping("/bank-card")
	public String bankCard() {
		return "bank-card";
	}

	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	/**
	 * 我的主页
	 *
	 * @return
	 */
	@GetMapping("/")
	public String home() {
		return "my-home-page";
	}

	/**
	 * 我的主页
	 *
	 * @return
	 */
	@GetMapping("/my-home-page")
	public String myHomePage() {
		return "my-home-page";
	}

	/**
	 * 个人信息
	 *
	 * @return
	 */
	@GetMapping("/personal-info")
	public String personalInfo() {
		return "personal-info";
	}

	/**
	 * 银行卡信息
	 *
	 * @return
	 */
	@GetMapping("/bank-card-info")
	public String bankCardInfo() {
		return "bank-card-info";
	}

	/**
	 * 虚拟钱包信息
	 *
	 * @return
	 */
	@GetMapping("/virtual-wallet-info")
	public String virtualWalletInfo() {
		return "virtual-wallet-info";
	}

	/**
	 * 个人帐变
	 *
	 * @return
	 */
	@GetMapping("/personal-account-change")
	public String personalAccountChange() {
		return "personal-account-change";
	}

	/**
	 * 充值
	 *
	 * @return
	 */
	@GetMapping("/recharge")
	public String recharge() {
		return "recharge";
	}

	/**
	 * 提现
	 *
	 * @return
	 */
	@GetMapping("/withdraw")
	public String withdraw() {
		return "withdraw";
	}

	/**
	 * 提现到银行卡
	 *
	 * @return
	 */
	@GetMapping("/withdraw-bank-card")
	public String withdrawBankCard() {
		return "withdraw-bank-card";
	}

	/**
	 * 提现到虚拟钱包
	 *
	 * @return
	 */
	@GetMapping("/withdraw-virtual-wallet")
	public String withdrawVirtualWallet() {
		return "withdraw-virtual-wallet";
	}

	/**
	 * 个人充提
	 *
	 * @return
	 */
	@GetMapping("/recharge-withdraw-log")
	public String rechargeWithdrawLog() {
		return "recharge-withdraw-log";
	}

	/**
	 * 收款码
	 *
	 * @return
	 */
	@GetMapping("/gathering-code")
	public String gatheringCode() {
		return "gathering-code";
	}

	/**
	 * 实名认证
	 *
	 * @return
	 */
	@GetMapping("/realname-certification")
	public String realnameCertification() {
		return "realname-certification";
	}

	/**
	 * 接单
	 *
	 * @return
	 */
	@GetMapping("/receive-order")
	public String receiveOrder() {
		return "receive-order";
	}

	/**
	 * 审核订单
	 *
	 * @return
	 */
	@GetMapping("/audit-order")
	public String auditOrder() {
		return "audit-order";
	}

	/**
	 * 接单记录
	 *
	 * @return
	 */
	@GetMapping("/receive-order-record")
	public String receiveOrderRecord() {
		return "receive-order-record";
	}

	/**
	 * 申诉记录
	 *
	 * @return
	 */
	@GetMapping("/appeal-record")
	public String appealRecord() {
		return "appeal-record";
	}

	/**
	 * 申诉详情
	 *
	 * @return
	 */
	@GetMapping("/appeal-details")
	public String appealDetails() {
		return "appeal-details";
	}

	/**
	 * 在线客服
	 *
	 * @return
	 */
	@GetMapping("/online-customer")
	public String onlineCustomer() {
		return "online-customer";
	}

	/**
	 * 代理中心
	 *
	 * @return
	 */
	@GetMapping("/agent-center")
	public String agentCenter() {
		return "agent-center";
	}

	/**
	 * 代理开户
	 *
	 * @return
	 */
	@GetMapping("/agent-open-an-account")
	public String agentOpenAnAccount() {
		return "agent-open-an-account";
	}

	/**
	 * 下级开户
	 *
	 * @return
	 */
	@GetMapping("/lower-level-open-an-account")
	public String lowerLevelOpenAnAccount() {
		return "lower-level-open-an-account";
	}

	/**
	 * 下级账号管理
	 *
	 * @return
	 */
	@GetMapping("/lower-level-account-manage")
	public String LowerLevelAccountManage() {
		return "lower-level-account-manage";
	}

	@GetMapping("/lower-level-account-change")
	public String LowerLevelAccountChange() {
		return "lower-level-account-change";
	}

	/**
	 * 团队接单明细
	 *
	 * @return
	 */
	@GetMapping("/lower-level-account-receive-order-record")
	public String LowerLevelAccountReceiveOrderRecord() {
		return "lower-level-account-receive-order-record";
	}

	/**
	 * 团队业绩明细
	 *
	 * @return
	 */
	@GetMapping("/lower-level-account-receive-results-record")
	public String LowerLevelAccountReceiveResultsRecord() {
		return "lower-level-account-receive-results-record";
	}

	/**
	 * 团队充值明细
	 *
	 * @return
	 */
	@GetMapping("/lower-level-recharge-details")
	public String lowerLevelRechargeDetails() {
		return "lower-level-recharge-details";
	}

	/**
	 * 团队提现明细
	 *
	 * @return
	 */
	@GetMapping("/lower-level-withdraw-details")
	public String lowerLevelWithdrawDetails() {
		return "lower-level-withdraw-details";
	}

	@GetMapping("/ranking-list")
	public String rankingList() {
		return "ranking-list";
	}

	@GetMapping("/receive-order-situation")
	public String receiveOrderSituation() {
		return "receive-order-situation";
	}

	@GetMapping("/system-notice")
	public String systemNotice() {
		return "system-notice";
	}

	@GetMapping("/pay")
	public String pay() {
		return "pay";
	}

	//收银台
	@GetMapping("/pay-online")
	public String payOnline() {
		return "pay-online";
	}

	//H5收银台
	@GetMapping("/pay-online-h5")
	public String payOnlineH5() {
		return "pay-online-h5";
	}

	//支付宝跳转
	@GetMapping("/paycheck")
	public String paycheck() {
		return "paycheck";
	}

	//支付宝转卡跳转
	@GetMapping("/payzfbcard")
	public String payzfbcard() {
		return "payzfbcard";
	}

	@ResponseBody
		@GetMapping("/paySuccessNotice")
	public String paySuccessNotice(@RequestParam Map<String, String> map) {
		System.err.println("支付成功回调");
		System.err.println(JSON.toJSONString(map));
		return "success";
	}

}
