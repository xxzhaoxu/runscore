package me.zohar.runscore.useraccount.service;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import cn.hutool.core.util.StrUtil;
import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.common.valid.ParamValid;
import me.zohar.runscore.useraccount.domain.VirtualWallet;
import me.zohar.runscore.useraccount.param.AddOrUpdateVirtualWalletParam;
import me.zohar.runscore.useraccount.repo.VirtualWalletRepo;
import me.zohar.runscore.useraccount.vo.VirtualWalletVO;

@Validated
@Service
public class VirtualWalletService {

	@Autowired
	private VirtualWalletRepo virtualWalletRepo;

	@Transactional(readOnly = true)
	public VirtualWalletVO findMyVirtualWalletById(@NotBlank String id, @NotBlank String userAccountId) {
		return VirtualWalletVO.convertFor(virtualWalletRepo.findByIdAndUserAccountId(id, userAccountId));
	}

	@Transactional(readOnly = true)
	public List<VirtualWalletVO> findVirtualWalletByUserAccountId(@NotBlank String userAccountId) {
		return VirtualWalletVO.convertFor(virtualWalletRepo.findByUserAccountIdAndDeletedFlagFalse(userAccountId));
	}

	@Transactional
	public void delVirtualWallet(@NotBlank String id, @NotBlank String userAccountId) {
		VirtualWallet virtualWallet = virtualWalletRepo.findByIdAndUserAccountId(id, userAccountId);
		virtualWallet.deleted();
		virtualWalletRepo.save(virtualWallet);
	}

	@ParamValid
	@Transactional
	public void addOrUpdateVirtualWallet(AddOrUpdateVirtualWalletParam param, String userAccountId) {
		// 新增
		if (StrUtil.isBlank(param.getId())) {
			VirtualWallet virtualWallet = param.convertToPo();
			virtualWallet.setUserAccountId(userAccountId);
			virtualWalletRepo.save(virtualWallet);
		}
		// 修改
		else {
			VirtualWallet virtualWallet = virtualWalletRepo.getOne(param.getId());
			if (!virtualWallet.getUserAccountId().equals(userAccountId)) {
				throw new BizException(BizError.无权修改银行卡);
			}
			BeanUtils.copyProperties(param, virtualWallet);
			virtualWalletRepo.save(virtualWallet);
		}
	}

}
