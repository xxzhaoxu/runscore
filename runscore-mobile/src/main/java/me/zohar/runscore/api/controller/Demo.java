package me.zohar.runscore.api.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpUtil;

/**
 * Hello world!
 *
 */
public class Demo {

	public static void test1() {
		String merchantNum = "huongshun";
		String orderNo = "123456";
		Double amount = 1d;
		String notifyUrl = "https://www.baidu.com/";
		String returnUrl = "https://www.baidu.com/";
		String payType = "wechat";
		String attch = "1";
		String securityKey = "7a427c6e0887ab17a90cd1254c04753f";
		String key = merchantNum + orderNo + String.valueOf(amount) + notifyUrl + securityKey;
		String sign = new Digester(DigestAlgorithm.MD5).digestHex(key);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("merchantNum", merchantNum);
		paramMap.put("orderNo", orderNo);
		paramMap.put("amount", amount);
		paramMap.put("notifyUrl", notifyUrl);
		paramMap.put("returnUrl", returnUrl);
		paramMap.put("payType", payType);
		paramMap.put("attch", attch);
		paramMap.put("sign", sign);
		String result = HttpUtil.post("http://47.105.220.94:8084/api/startOrder", paramMap);
		System.out.println(result);
	}


	public static void main(String[] args) throws NoSuchAlgorithmException {
		test1();
	}

}
