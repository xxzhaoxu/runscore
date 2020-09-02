package me.zohar.runscore.zfb.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AntCertificationUtil;
import com.alipay.api.request.AlipayFundTransAppPayRequest;
import com.alipay.api.response.AlipayFundTransAppPayResponse;

public class Test {
	private final static String gateway = "https://openapi.alipay.com/gateway.do";
	private final static String app_id = "2019011663024714";
	private final static String app_privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQEokkefE3NFVYbp6v+Wf3uQy9WL/1d20NvUZOv0rVv1xEXs7qsvkA7x6Xus6pCyLfyyI/T0/3YO6jUcPRiih7/I4//zKlRBcgQssa5jqPDwBXIjPHufXectqYXhAHy1zh1kTbTYcSbTUvJlTd9YI+aCIPO4gncrz09YUbbzsXbugBmEJMY3iHyIDW8POCy2PRm7NFRaqGfI+ipTglHmmCffgfPzUG58Eisr4AyJlesKmyY7skmfAEgM523W9IPcPtasiUmX3aaiwNLgk/PRwCMD6HqFKTCyRiYqK614irde8kbcxtqVAAEoYPpTpy8T1isKql9Tdz3cijzk8h8OFLAgMBAAECggEAFgJ+6Q+oBiL8ltHvdDrJ41Jl29dfHATuqBauUwFuUVE2q0tAmSJWYAonxk5HnBOTdFT4Gmvnb4djbZfkLnLI1Ig0wxodhpnsXoVVufpKH+jOyID1ICIMqbsqIfTcxhQPUIEHa+lgJTSB134noPEMwLPv4BvcJQqmS1Xmy9YmVOHlfZNgcqwgiDN50iC1KE4NOaC9RyV9m5CaewSsxjKLz57B8Lt02PGYCyVZHlJcXDXqUwnvASdOJcyHXPJSMltzC6dh6gqbFLSGnohTREzE3KaTfQ4M1M+tEi6cxd69N/tY18XcEVomIRJlqdPnTOTWkOUOtYwgzGUVr0Uf2tQVgQKBgQDYi0Ek85kjQpJ706HLfGV960KS5EZDzh191J7MXMWiF0qKZU2x15K2RE9HWcG5xiU+/D1Ii5WUJLgYSDbXwOCbe1z8JI8t6rLNSmrUYHTwhlgo7HOCSOA/gxPzDalSYKlJSGt60h0amVMiMbsj2l1atUB1HTBJ7PyZ0X6Y5zTSmwKBgQCqUtPxhQ/MuMi8WuVrgImNvrd0P8n+RKOlMi0WTsGqAoFtIC9ZjD1kI0muz6Ts7a7xy58vUpdZZCom9NIvFhnS3MTv5DbGL8B9d9H9n+FnEzqS58XLaNZ3l7isFZ46nttaz3ZOJnxebzkh5GuK3Wgf5bNQKXoAOvEVwSjEPhx/EQKBgQCFNBQcoORgtkDZfvOV8q2T6bYIGffHGfE2SCZu29qOvPkTbQGAFnWfPSHvaB0tI+AhgJRKHeMjP/smpLVnvrPrsNUgHXUd77ORaOeSQMtucZWlht3/FMktCHqrFopDrEY2WCAVa06P4CUtcZ4GjfrDI5/E6vGbZ/ZywFW5xO/PZQKBgEeeyKyYjUpCsXYyqqIHzM+j0LS7Vg7rgfynGrp0p477urzOedqn0/DVvdgaTHmsgJIAU5GsoyPdsLPs4q0WGAnI968AQeAYmFKqHc0Oe9PCDDFV8KBWF0j2rZr/BCgWQJrw/ghiuo1jK50K1gXwkCZJips8taZ/In1JWgEbNRRhAoGAWffIhnmT4ATYifz3U3fVYVdFKR1gkGn2BJQeCvvDo6Z4ZCxRTrsHtsF3aVfXqHVU7pCOcUfjnDQJ2v7hFNBNPWUoaY9TciPkOUklkYT6wjOj5QwcyFUSME6dFbWnqlWHcCzYhDzeS88RXBWJk1SrN3q6PrNXC6qGCAPSWYLLINU=";
	private final static String charset = "utf-8";
	private final static String sign_type = "RSA2";
	private final static String app_cert_path = "";
	private final static String alipay_cert_path = "";
	private final static String alipay_root_cert_path = "";

	public static void main(String[] args) {
		try {
			zfb();
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
	}

	public static void zfb() throws AlipayApiException{
		CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
		certAlipayRequest.setServerUrl(gateway);
		certAlipayRequest.setAppId(app_id);
		certAlipayRequest.setPrivateKey(app_privateKey);
		certAlipayRequest.setFormat("json");
		certAlipayRequest.setCharset(charset);
		certAlipayRequest.setSignType(sign_type);
		certAlipayRequest.setCertPath(app_cert_path);
		certAlipayRequest.setAlipayPublicCertPath(alipay_cert_path);
		certAlipayRequest.setRootCertPath(alipay_root_cert_path);
		DefaultAlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
		AlipayFundTransAppPayRequest request = new AlipayFundTransAppPayRequest();
		request.setBizContent("{" +
		"\"out_biz_no\":\"2018062800001\"," +
		"\"trans_amount\":8.88," +
		"\"product_code\":\"STD_RED_PACKET\"," +
		"\"biz_scene\":\"PERSONAL_PAY\"," +
		"\"remark\":\"拼手气红包\"," +
		"\"order_title\":\"群聊拼手气红包\"," +
		"\"request_time\":\"2019-06-23 19:15\"," +
		"\"time_expire\":\"2018-03-23 19:15\"," +
		"\"refund_time_expire\":\"2018-11-08 10:00\"," +
		"\"business_params\":\"{\\\"sub_biz_scene\\\":\\\"REDPACKET\\\",\\\"payer_binded_alipay_uid:\\\"2088302510459335\\\"}\"," +
		"  }");
		AlipayFundTransAppPayResponse response = alipayClient.sdkExecute(request);
		if(response.isSuccess()){
		System.out.println("调用成功");
		} else {
		System.out.println("调用失败");
		}
	}
}
