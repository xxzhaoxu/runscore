package me.zohar.runscore.agent.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import me.zohar.runscore.agent.domain.InviteCode;
import me.zohar.runscore.agent.domain.InviteCodeChannelRebate;
import me.zohar.runscore.agent.param.ChannelRebateParam;
import me.zohar.runscore.agent.param.GenerateInviteCodeParam;
import me.zohar.runscore.agent.param.InviteCodeQueryCondParam;
import me.zohar.runscore.agent.param.ManualTopScoreParam;
import me.zohar.runscore.agent.repo.InviteCodeChannelRebateRepo;
import me.zohar.runscore.agent.repo.InviteCodeRepo;
import me.zohar.runscore.agent.vo.InviteCodeDetailsVO;
import me.zohar.runscore.common.exception.BizError;
import me.zohar.runscore.common.exception.BizException;
import me.zohar.runscore.common.valid.ParamValid;
import me.zohar.runscore.common.vo.PageResult;
import me.zohar.runscore.constants.Constant;
import me.zohar.runscore.mastercontrol.domain.RegisterSetting;
import me.zohar.runscore.mastercontrol.repo.RegisterSettingRepo;
import me.zohar.runscore.useraccount.domain.AccountChangeLog;
import me.zohar.runscore.useraccount.domain.AccountReceiveOrderChannel;
import me.zohar.runscore.useraccount.domain.UserAccount;
import me.zohar.runscore.useraccount.repo.AccountChangeLogRepo;
import me.zohar.runscore.useraccount.repo.AccountReceiveOrderChannelRepo;
import me.zohar.runscore.useraccount.repo.UserAccountRepo;

@Validated
@Service
public class AgentService {

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private InviteCodeRepo inviteCodeRepo;

	@Autowired
	private RegisterSettingRepo inviteRegisterSettingRepo;

	@Autowired
	private AccountReceiveOrderChannelRepo accountReceiveOrderChannelRepo;

	@Autowired
	private InviteCodeChannelRebateRepo inviteCodeChannelRebateRepo;

	@Autowired
	private AccountChangeLogRepo accountChangeLogRepo;

	@ParamValid
	@Transactional
	public void manualTopScore(ManualTopScoreParam param) {
		UserAccount currentAccount = userAccountRepo.getOne(param.getCurrentAccountId());
		UserAccount topScoreAccount = userAccountRepo.getOne(param.getTopScoreAccountId());
		Double cashDeposit = NumberUtil.round(currentAccount.getCashDeposit() - param.getTopScoreAmount(), 4)
				.doubleValue();
		if (cashDeposit < 0) {
			throw new BizException(BizError.账户余额不足);
		}
		currentAccount.setCashDeposit(cashDeposit);
		userAccountRepo.save(currentAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithTopScoreDeduction(currentAccount, param.getTopScoreAmount(),
				topScoreAccount.getUserName()));

		topScoreAccount.setCashDeposit(
				NumberUtil.round(topScoreAccount.getCashDeposit() + param.getTopScoreAmount(), 4).doubleValue());
		userAccountRepo.save(topScoreAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithTopScore(topScoreAccount, param.getTopScoreAmount()));
	}


	@ParamValid
	@Transactional
	public void showManualState(String currentAccountId,InviteCodeQueryCondParam param) {
		if(currentAccountId.equals(param.getUserAccountId())){
			throw new BizException(BizError.不能开启或关闭该账号);
		}

		//currentAccountId 操作账号
		UserAccount currentAccount = userAccountRepo.getOne(currentAccountId);
		if(currentAccount != null){
			if("0".equals(currentAccount.getState())){
				throw new BizException(BizError.你的账号已被禁用);
			}
		}

		UserAccount topScoreAccount = userAccountRepo.getOne(param.getUserAccountId());
		if(topScoreAccount != null){
			String state = "0";
			if("0".equals(param.getState())){
				state = "1";
			}
			topScoreAccount.setState(state);
			userAccountRepo.save(topScoreAccount);
			if("agent".equals(topScoreAccount.getAccountType())){
				updUserState(param.getUserAccountId(),state);
			}
		}
	}


	@Transactional(readOnly = true)
	public void updUserState(String id,String state){
		//uaList 查询第二级用户
		List<UserAccount> uaList = userAccountRepo.findByInviterId(id);
		for (UserAccount ua : uaList) {
			if("agent".equals(ua.getAccountType())){
				ua.setState(state);
				userAccountRepo.save(ua);
				updUserState(ua.getId(),state);
			}else{
				ua.setState(state);
				userAccountRepo.save(ua);
			}
		}
	}

	/**
	 * 生成邀请码
	 *
	 * @param param
	 * @return
	 */
	@ParamValid
	@Transactional
	public String generateInviteCode(GenerateInviteCodeParam param) {
		UserAccount currentAccount = userAccountRepo.getOne(param.getInviterId());
		if(currentAccount != null){
			if("0".equals(currentAccount.getState())){
				throw new BizException(BizError.你的账号已被禁用);
			}
		}
		RegisterSetting setting = inviteRegisterSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (setting == null || !setting.getInviteRegisterEnabled()) {
			throw new BizException(BizError.邀请注册功能已关闭);
		}
		UserAccount inviter = userAccountRepo.getOne(param.getInviterId());
		if (!Constant.账号类型_代理.equals(inviter.getAccountType())) {
			throw new BizException(BizError.只有代理才能进行下级开户操作);
		}
		if (!(Constant.账号类型_代理.equals(param.getAccountType()) || Constant.账号类型_会员.equals(param.getAccountType()))) {
			throw new BizException(BizError.开户类型只能是代理或会员);
		}
		if (setting.getOnlyOpenMemberAccount()) {
			/*if (!Constant.账号类型_会员.equals(param.getAccountType())) {
				throw new BizException(BizError.开户类型只能是会员);
			}*/
		}
		if (inviter.getInviteCodeQuota() <= 0) {
			throw new BizException(BizError.邀请码配额不足);
		}

		Map<String, String> channelMap = new HashMap<>();
		for (ChannelRebateParam channelRebateParam : param.getChannelRebates()) {
			if (channelMap.get(channelRebateParam.getChannelId()) != null) {
				throw new BizException(BizError.不能设置重复的通道);
			}
			channelMap.put(channelRebateParam.getChannelId(), channelRebateParam.getChannelId());
			AccountReceiveOrderChannel receiveOrderChannel = accountReceiveOrderChannelRepo
					.findByUserAccountIdAndChannelId(inviter.getId(), channelRebateParam.getChannelId());
			if (receiveOrderChannel == null) {
				throw new BizException(BizError.未知通道不能进行下级开户操作);
			}
			if (channelRebateParam.getRebate() > receiveOrderChannel.getRebate()) {
				throw new BizException(BizError.下级账号的返点不能大于上级账号);
			}
		}

		String code = IdUtil.fastSimpleUUID().substring(0, 6);
		while (inviteCodeRepo.findTopByUsedFalseAndCodeAndPeriodOfValidityGreaterThanEqual(code, new Date()) != null) {
			code = IdUtil.fastSimpleUUID().substring(0, 6);
		}
		InviteCode inviteCode = param.convertToPo(code, setting.getInviteCodeEffectiveDuration());
		inviteCodeRepo.save(inviteCode);
		for (ChannelRebateParam channelRebateParam : param.getChannelRebates()) {
			InviteCodeChannelRebate channelRebate = channelRebateParam.convertToPo(inviteCode.getId());
			inviteCodeChannelRebateRepo.save(channelRebate);
		}
		inviter.setInviteCodeQuota(inviter.getInviteCodeQuota() - 1);
		userAccountRepo.save(inviter);
		return inviteCode.getId();
	}

	@Transactional(readOnly = true)
	public InviteCodeDetailsVO getInviteCodeDetailsInfoById(@NotBlank String id){
		InviteCode inviteCode = inviteCodeRepo.getOne(id);
		InviteCodeDetailsVO inviteDetailsInfo = InviteCodeDetailsVO.convertFor(inviteCode);
		return inviteDetailsInfo;
	}

	@Transactional(readOnly = true)
	public PageResult<InviteCodeDetailsVO> findInviteCodeByPage(InviteCodeQueryCondParam param) {
		Specification<InviteCode> spec = new Specification<InviteCode>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<InviteCode> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getUserAccountId())) {
					predicates.add(builder.equal(root.get("inviterId"), param.getUserAccountId()));
				}
				if (StrUtil.isNotEmpty(param.getState())) {
					if (Constant.邀请码状态_未使用.equals(param.getState())) {
						predicates.add(builder.isFalse(root.get("used")));
						predicates.add(builder.greaterThan(root.get("periodOfValidity"), new Date()));
					}
					if (Constant.邀请码状态_已使用.equals(param.getState())) {
						predicates.add(builder.isTrue(root.get("used")));
					}
					if (Constant.邀请码状态_已失效.equals(param.getState())) {
						predicates.add(builder.isFalse(root.get("used")));
						predicates.add(builder.lessThanOrEqualTo(root.get("periodOfValidity"), new Date()));
					}
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<InviteCode> result = inviteCodeRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<InviteCodeDetailsVO> pageResult = new PageResult<>(
				InviteCodeDetailsVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

}
