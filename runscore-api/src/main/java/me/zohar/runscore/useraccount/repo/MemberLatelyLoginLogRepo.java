package me.zohar.runscore.useraccount.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.useraccount.domain.MemberLatelyLoginLog;

public interface MemberLatelyLoginLogRepo
		extends JpaRepository<MemberLatelyLoginLog, String>, JpaSpecificationExecutor<MemberLatelyLoginLog> {
	
	List<MemberLatelyLoginLog> findByLastAccessTimeLessThan(Date lastAccessTime);

}
