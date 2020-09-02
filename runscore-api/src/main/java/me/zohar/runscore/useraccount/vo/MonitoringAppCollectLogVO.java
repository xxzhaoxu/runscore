package me.zohar.runscore.useraccount.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import me.zohar.runscore.useraccount.domain.MonitoringAppCollectLog;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;

@Data
public class MonitoringAppCollectLogVO {

	private String id;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private String amount;

	private String secretKey;

	private String accountId;

	private String userName;

	private String wechatPayee;

	private String alipayPayee;

	private String bankPayee;

	public static List<MonitoringAppCollectLogVO> convertFor(List<MonitoringAppCollectLog> logs) {
		if (CollectionUtil.isEmpty(logs)) {
			return new ArrayList<>();
		}
		List<MonitoringAppCollectLogVO> vos = new ArrayList<>();
		for (MonitoringAppCollectLog log : logs) {
			vos.add(convertFor(log));
		}
		return vos;
	}

	public static MonitoringAppCollectLogVO convertFor(MonitoringAppCollectLog log) {
		if (log == null) {
			return null;
		}
		MonitoringAppCollectLogVO vo = new MonitoringAppCollectLogVO();
		BeanUtils.copyProperties(log, vo);
		if (log.getUserAccount() != null) {
			vo.setUserName(log.getUserAccount().getUserName());
		}
		return vo;
	}

}
