package me.zohar.runscore.mastercontrol.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.mastercontrol.domain.WithdrawSetting;

public interface WithdrawSettingRepo
		extends JpaRepository<WithdrawSetting, String>, JpaSpecificationExecutor<WithdrawSetting> {
	
	WithdrawSetting findTopByOrderByLatelyUpdateTime();

}
