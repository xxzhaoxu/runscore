package me.zohar.runscore.useraccount.repo;

import java.util.Date;

import me.zohar.runscore.useraccount.domain.OperLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OperLogRepo extends JpaRepository<OperLog, String>, JpaSpecificationExecutor<OperLog> {

	long deleteByOperTimeGreaterThanEqualAndOperTimeLessThanEqual(Date startTime, Date endTime);

}
