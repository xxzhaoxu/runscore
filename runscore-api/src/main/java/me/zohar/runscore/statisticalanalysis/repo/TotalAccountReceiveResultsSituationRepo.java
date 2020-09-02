package me.zohar.runscore.statisticalanalysis.repo;

import java.util.List;

import me.zohar.runscore.statisticalanalysis.domain.TotalAccountReceiveResultsSituation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TotalAccountReceiveResultsSituationRepo
		extends JpaRepository<TotalAccountReceiveResultsSituation, String>, JpaSpecificationExecutor<TotalAccountReceiveResultsSituation> {

	List<TotalAccountReceiveResultsSituation> findByInviterIdAndDeletedFlagIsFalse(String inviterId);
}
