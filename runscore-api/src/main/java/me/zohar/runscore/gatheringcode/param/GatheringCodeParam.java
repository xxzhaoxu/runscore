package me.zohar.runscore.gatheringcode.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.gatheringcode.domain.GatheringCode;

@Data
public class GatheringCodeParam {

	/**
	 * 主键id
	 */
	private String id;

	/**
	 * 所属账号
	 */
	private String userName;

	/**
	 * 收款通道id
	 */
	@NotBlank
	private String gatheringChannelId;

	/**
	 * 收款金额
	 */
	private Double gatheringAmount;

	private Boolean fixedGatheringAmount;

	/**
	 * 收款人
	 */
	private String payee;

	private String storageId;

	/**
	 * 支付宝userid
	 */
	private String alipayUserid;

	/**
	 * 银行
	 */
	private String openAccountBank;

	/**
	 * 银行编号大写
	 */
	private String bankShortName;

	/**
	 * 开户人
	 */
	private String accountHolder;

	/**
	 * 卡号
	 */
	private String bankCardAccount;

	private String mobile;

	private String realName;

	private String account;

	private String googleVerCode;

	private String groupNickName;

	public GatheringCode convertToPo(String userAccountId) {
		GatheringCode po = new GatheringCode();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCreateTime(new Date());
		po.setUserAccountId(userAccountId);
		po.setInUse(true);
		po.setState(Constant.收款码状态_正常);



		if(po.getStorageId() != null && po.getStorageId() != ""){
			String s[] = po.getStorageId().split(",");
			if(s.length > 1){
				po.setStorageId(s[0]);
				po.setCodeContent(s[1]);
			}
		}
		return po;
	}

}
