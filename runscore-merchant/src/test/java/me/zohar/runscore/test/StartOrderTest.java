package me.zohar.runscore.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpUtil;

public class StartOrderTest {

	/**
	 * 发起订单接口生成签名例子
	 * 
	 * @param args
	 */
	@Test
	public void test1() {
		String merchantNum = "1001";// 商户号
		String orderNo = "20190629023134U936283877";// 商户订单号
		String amount = "10.00";// 支付金额
		String notifyUrl = "localhost:8080/paySuccessNotice";// 异步通知地址
		String returnUrl = "localhost:8080/paySuccessNotice";// 同步通知地址
		String payType = "wechat";// 请求支付类型
		String attch = ""; // 附加参数
		String secretKey = "fd45643dkfjdka";//商户密钥
		String sign = merchantNum + orderNo + amount + notifyUrl + secretKey;
		sign = new Digester(DigestAlgorithm.MD5).digestHex(sign);// md5签名

		String url = "http://193.112.110.196:8080/api/startOrder";// 发起订单地址
		Map<String, Object> paramMap = new HashMap<>();// post请求的参数
		paramMap.put("merchantNum", merchantNum);
		paramMap.put("orderNo", orderNo);
		paramMap.put("amount", amount);
		paramMap.put("notifyUrl", notifyUrl);
		paramMap.put("returnUrl", returnUrl);
		paramMap.put("payType", payType);
		paramMap.put("attch", attch);
		paramMap.put("sign", sign);
		String result = HttpUtil.post(url, paramMap);
		System.out.println(result);
	}

}
