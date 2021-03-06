package me.zohar.runscore.useraccount.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.useraccount.domain.LoginLog;

public interface LoginLogRepo extends JpaRepository<LoginLog, String>, JpaSpecificationExecutor<LoginLog> {

	LoginLog findTopBySessionIdOrderByLoginTime(String sessionId);

	long deleteByLoginTimeGreaterThanEqualAndLoginTimeLessThanEqual(Date startTime, Date endTime);

}
