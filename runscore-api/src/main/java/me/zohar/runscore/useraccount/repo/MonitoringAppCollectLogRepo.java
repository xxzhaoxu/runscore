package me.zohar.runscore.useraccount.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.useraccount.domain.MonitoringAppCollectLog;

public interface MonitoringAppCollectLogRepo
		extends JpaRepository<MonitoringAppCollectLog, String>, JpaSpecificationExecutor<MonitoringAppCollectLog> {

}
