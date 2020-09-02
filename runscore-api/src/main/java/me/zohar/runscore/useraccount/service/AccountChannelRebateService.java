package me.zohar.runscore.useraccount.service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import me.zohar.runscore.agent.param.AdjustRebateParam;
import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.useraccount.domain.AccountReceiveOrderChannel;
import me.zohar.runscore.useraccount.domain.UserAccount;
import me.zohar.runscore.useraccount.param.AccountReceiveOrderChannelParam;
import me.zohar.runscore.useraccount.param.SaveAccountReceiveOrderChannelParam;
import me.zohar.runscore.useraccount.repo.AccountReceiveOrderChannelRepo;
import me.zohar.runscore.useraccount.repo.UserAccountRepo;
import me.zohar.runscore.useraccount.vo.AccountReceiveOrderChannelVO;

@Validated
@Service
public class AccountChannelRebateService {

	@Autowired
	private AccountReceiveOrderChannelRepo accountReceiveOrderChannelRepo;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Transactional
	public void adjustRebate(String superiorAccountId, @NotEmpty List<AdjustRebateParam> params) {
		UserAccount superiorAccount = userAccountRepo.getOne(superiorAccountId);
		for (AdjustRebateParam param : params) {
			AccountReceiveOrderChannel accountReceiveOrderChannel = accountReceiveOrderChannelRepo
					.getOne(param.getId());
			UserAccount userAccount = accountReceiveOrderChannel.getUserAccount();
			if (!userAccount.getAccountLevelPath().startsWith(superiorAccount.getAccountLevelPath())) {
				throw new BizException(BizError.无权调整该账号的返点);
			}
			AccountReceiveOrderChannel superiorChannel = accountReceiveOrderChannelRepo.findByUserAccountIdAndChannelId(
					superiorAccount.getId(), accountReceiveOrderChannel.getChannelId());
			if (superiorChannel == null) {
				throw new BizException(BizError.参数异常 + "", MessageFormat.format("[{0}]通道未设置,无法调整下级的返点",
						accountReceiveOrderChannel.getChannel().getChannelName()));
			}
			if (param.getRebate() > superiorChannel.getRebate()) {
				throw new BizException(BizError.下级账号的返点不能大于上级账号);
			}
			accountReceiveOrderChannel.setRebate(param.getRebate());
			accountReceiveOrderChannelRepo.save(accountReceiveOrderChannel);
		}
	}

	@Transactional(readOnly = true)
	public List<AccountReceiveOrderChannelVO> findAccountReceiveOrderChannelByAccountId(
			@NotBlank String userAccountId) {
		List<AccountReceiveOrderChannel> channels = accountReceiveOrderChannelRepo
				.findByUserAccountIdAndChannelDeletedFlagFalse(userAccountId);
		return AccountReceiveOrderChannelVO.convertFor(channels);
	}

	@Transactional
	public void saveAccountReceiveOrderChannel(SaveAccountReceiveOrderChannelParam param) {
		Map<String, String> map = new HashMap<>();
		for (AccountReceiveOrderChannelParam channelParam : param.getChannels()) {
			if (map.get(channelParam.getChannelId()) != null) {
				throw new BizException(BizError.不能设置重复的接单通道);
			}
			map.put(channelParam.getChannelId(), channelParam.getChannelId());
		}

		List<AccountReceiveOrderChannel> channels = accountReceiveOrderChannelRepo
				.findByUserAccountId(param.getUserAccountId());
		accountReceiveOrderChannelRepo.deleteAll(channels);
		for (AccountReceiveOrderChannelParam channelParam : param.getChannels()) {
			AccountReceiveOrderChannel po = channelParam.convertToPo();
			po.setUserAccountId(param.getUserAccountId());
			accountReceiveOrderChannelRepo.save(po);
		}
	}

}
