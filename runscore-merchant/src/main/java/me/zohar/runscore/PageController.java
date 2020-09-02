package me.zohar.runscore;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	@GetMapping("/")
	public String index() {
		return "statistical-analysis";
	}

	/**
	 * 登录页面
	 * 
	 * @return
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	/**
	 * 商户订单
	 * 
	 * @return
	 */
	@GetMapping("/merchant-order")
	public String merchantOrder() {
		return "merchant-order";
	}

	/**
	 * 统计分析
	 * 
	 * @return
	 */
	@GetMapping("/statistical-analysis")
	public String statisticalAnalysis() {
		return "statistical-analysis";
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

	@GetMapping("/appeal-details")
	public String appealDetails() {
		return "appeal-details";
	}

	@GetMapping("/merchant-info")
	public String merchantInfo() {
		return "merchant-info";
	}

	@GetMapping("/rate-details")
	public String rateDetails() {
		return "rate-details";
	}

	@GetMapping("/apply-settlement")
	public String applySettlement() {
		return "apply-settlement";
	}

}
