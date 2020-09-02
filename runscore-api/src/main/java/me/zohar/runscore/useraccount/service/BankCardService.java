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
import me.zohar.runscore.useraccount.domain.BankCard;
import me.zohar.runscore.useraccount.param.AddOrUpdateBankCardParam;
import me.zohar.runscore.useraccount.repo.BankCardRepo;
import me.zohar.runscore.useraccount.vo.BankCardVO;

@Validated
@Service
public class BankCardService {

	@Autowired
	private BankCardRepo bankCardRepo;

	@Transactional(readOnly = true)
	public BankCardVO findMyBankCardById(@NotBlank String id, @NotBlank String userAccountId) {
		return BankCardVO.convertFor(bankCardRepo.findByIdAndUserAccountId(id, userAccountId));
	}

	@Transactional(readOnly = true)
	public List<BankCardVO> findBankCard(@NotBlank String userAccountId) {
		return BankCardVO.convertFor(bankCardRepo.findByUserAccountIdAndDeletedFlagFalse(userAccountId));
	}

	@Transactional
	public void delBankCard(@NotBlank String id) {
		BankCard bankCard = bankCardRepo.getOne(id);
		delBankCard(bankCard.getId(), bankCard.getUserAccountId());
	}

	@Transactional
	public void delBankCard(@NotBlank String id, @NotBlank String userAccountId) {
		BankCard bankCard = bankCardRepo.findByIdAndUserAccountId(id, userAccountId);
		bankCard.deleted();
		bankCardRepo.save(bankCard);
	}

	@ParamValid
	@Transactional
	public void addOrUpdateBankCard(AddOrUpdateBankCardParam param, String userAccountId) {
		// 新增
		if (StrUtil.isBlank(param.getId())) {
			BankCard bankCard = param.convertToPo();
			bankCard.setUserAccountId(userAccountId);
			bankCardRepo.save(bankCard);
		}
		// 修改
		else {
			BankCard bankCard = bankCardRepo.getOne(param.getId());
			if (!bankCard.getUserAccountId().equals(userAccountId)) {
				throw new BizException(BizError.无权修改银行卡);
			}
			BeanUtils.copyProperties(param, bankCard);
			bankCardRepo.save(bankCard);
		}
	}

}
