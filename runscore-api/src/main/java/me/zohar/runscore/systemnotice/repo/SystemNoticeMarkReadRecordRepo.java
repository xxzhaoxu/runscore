package me.zohar.runscore.systemnotice.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.systemnotice.domain.SystemNoticeMarkReadRecord;

public interface SystemNoticeMarkReadRecordRepo extends JpaRepository<SystemNoticeMarkReadRecord, String>,
		JpaSpecificationExecutor<SystemNoticeMarkReadRecord> {

	List<SystemNoticeMarkReadRecord> findBySystemNoticeIdAndUserAccountId(String systemNoticeId, String userAccountId);

}
