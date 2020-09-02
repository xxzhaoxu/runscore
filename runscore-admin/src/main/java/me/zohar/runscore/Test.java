package me.zohar.runscore;

public class Test {

	public static void main(String[] args) {
		String str = "" ;
		str = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=2019022163313167&amp;scope=auth_base&amp;redirect_uri=http://pay.weimifu.net/zfbsj.php?trade_no=3739348067833348096&amp;type=9&amp;domain=http://bypay.suoqing.xyz/";
		str = str.replaceAll("amp;", "");
		System.out.println(str);
	}

}
