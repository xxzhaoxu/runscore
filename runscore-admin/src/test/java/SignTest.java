import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class SignTest {

	public static void main(String[] args) {
		String sign = "1"+ "xiaojian" + "8375201908141419374460" + "1"
				+ "6351a4f461e7d110e2c9e573fcc984d3";
		sign = new Digester(DigestAlgorithm.MD5).digestHex(sign);
		System.out.println(sign);
		// sign的值为：4f40f1e1d02cbcd23e471eee291d4ef6
	}

}
