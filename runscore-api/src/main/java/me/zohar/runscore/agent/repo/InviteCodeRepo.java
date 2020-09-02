package me.zohar.runscore.agent.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.agent.domain.InviteCode;

public interface InviteCodeRepo extends JpaRepository<InviteCode, String>, JpaSpecificationExecutor<InviteCode> {

	List<InviteCode> findByCreateTimeGreaterThanEqualAndCreateTimeLessThanEqual(Date startTime, Date endTime);

	InviteCode findTopByUsedFalseAndCodeAndPeriodOfValidityGreaterThanEqual(String code, Date periodOfValidity);

}
