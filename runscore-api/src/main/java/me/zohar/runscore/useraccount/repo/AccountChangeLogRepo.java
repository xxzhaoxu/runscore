package me.zohar.runscore.useraccount.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.useraccount.domain.AccountChangeLog;

public interface AccountChangeLogRepo
		extends JpaRepository<AccountChangeLog, String>, JpaSpecificationExecutor<AccountChangeLog> {
	
	long deleteByAccountChangeTimeGreaterThanEqualAndAccountChangeTimeLessThanEqual(Date startTime, Date endTime);

}
