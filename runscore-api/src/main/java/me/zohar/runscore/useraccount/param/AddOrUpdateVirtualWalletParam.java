package me.zohar.runscore.useraccount.param;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.useraccount.domain.VirtualWallet;

@Data
public class AddOrUpdateVirtualWalletParam {

	private String id;

	private String virtualWalletType;

	/**
	 * 电子钱包地址
	 */
	private String virtualWalletAddr;

	public VirtualWallet convertToPo() {
		VirtualWallet po = new VirtualWallet();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCreateTime(new Date());
		po.setLatelyModifyTime(po.getCreateTime());
		po.setDeletedFlag(false);
		return po;
	}

}
