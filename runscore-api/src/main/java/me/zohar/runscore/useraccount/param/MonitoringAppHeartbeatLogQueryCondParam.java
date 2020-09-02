package me.zohar.runscore.useraccount.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;



import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.runscore.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class MonitoringAppHeartbeatLogQueryCondParam extends PageParam {

	private String ipAddr;

	private String userName;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startTime;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;

}
