package me.zohar.runscore.useraccount.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.useraccount.domain.MonitoringAppHeartbeatLog;

public interface MonitoringAppHeartbeatLogRepo
		extends JpaRepository<MonitoringAppHeartbeatLog, String>, JpaSpecificationExecutor<MonitoringAppHeartbeatLog> {

}
