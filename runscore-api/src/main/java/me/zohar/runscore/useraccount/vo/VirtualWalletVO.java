package me.zohar.runscore.useraccount.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.useraccount.domain.VirtualWallet;

@Data
public class VirtualWalletVO {

	private String id;

	private String virtualWalletType;

	/**
	 * 电子钱包地址
	 */
	private String virtualWalletAddr;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 最近修改时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date latelyModifyTime;

	private String userAccountId;

	public static List<VirtualWalletVO> convertFor(List<VirtualWallet> virtualWallets) {
		if (CollectionUtil.isEmpty(virtualWallets)) {
			return new ArrayList<>();
		}
		List<VirtualWalletVO> vos = new ArrayList<>();
		for (VirtualWallet virtualWallet : virtualWallets) {
			vos.add(convertFor(virtualWallet));
		}
		return vos;
	}

	public static VirtualWalletVO convertFor(VirtualWallet virtualWallet) {
		if (virtualWallet == null) {
			return null;
		}
		VirtualWalletVO vo = new VirtualWalletVO();
		BeanUtils.copyProperties(virtualWallet, vo);
		return vo;
	}

}
