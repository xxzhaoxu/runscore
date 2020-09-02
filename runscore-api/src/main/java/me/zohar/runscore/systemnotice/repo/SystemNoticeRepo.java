package me.zohar.runscore.systemnotice.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.runscore.systemnotice.domain.SystemNotice;

public interface SystemNoticeRepo extends JpaRepository<SystemNotice, String>, JpaSpecificationExecutor<SystemNotice> {

	SystemNotice findTopByPublishTimeIsLessThanEqualOrderByPublishTimeDesc(Date publishTime);

	List<SystemNotice> findTop10ByPublishTimeIsLessThanEqualOrderByPublishTimeDesc(Date publishTime);
}
