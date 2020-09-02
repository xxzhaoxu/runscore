package me.zohar.runscore.merchant.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.merchant.domain.QueueRecord;

public interface QueueRecordRepo extends JpaRepository<QueueRecord, String>, JpaSpecificationExecutor<QueueRecord> {

	QueueRecord findByUserAccountIdAndId(String userAccountId, String id);

	QueueRecord findTopByUserAccountIdAndUsedIsFalse(String userAccountId);

	QueueRecord findTopByUserAccountIdNotInAndUsedIsFalseOrderByQueueTime(List<String> userAccountIds);

	List<QueueRecord> findByUserAccountIdNotInAndUsedIsFalseOrderByQueueTime(List<String> userAccountIds);

	QueueRecord findTopByUserAccountIdAndUsedIsTrueAndMarkReadIsFalseOrderByQueueTimeDesc(String userAccountId);

	List<QueueRecord> findByUsedIsFalseOrderByQueueTime();

}
