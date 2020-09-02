package me.zohar.runscore.useraccount.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import me.zohar.runscore.useraccount.domain.MonitoringAppHeartbeatLog;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class MonitoringAppHeartbeatLogVO {

	private String id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private String ipAddr;

	private String loginLocation;

	private String secretKey;

	private String accountId;

	private String userName;

	private String wechatPayee;

	private String alipayPayee;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lastHeartbeatTime;

	public static List<MonitoringAppHeartbeatLogVO> convertFor(List<MonitoringAppHeartbeatLog> logs) {
		if (CollectionUtil.isEmpty(logs)) {
			return new ArrayList<>();
		}
		List<MonitoringAppHeartbeatLogVO> vos = new ArrayList<>();
		for (MonitoringAppHeartbeatLog log : logs) {
			vos.add(convertFor(log));
		}
		return vos;
	}

	public static MonitoringAppHeartbeatLogVO convertFor(MonitoringAppHeartbeatLog log) {
		if (log == null) {
			return null;
		}
		MonitoringAppHeartbeatLogVO vo = new MonitoringAppHeartbeatLogVO();
		BeanUtils.copyProperties(log, vo);
		if (log.getUserAccount() != null) {
			vo.setUserName(log.getUserAccount().getUserName());
		}
		return vo;
	}

}
