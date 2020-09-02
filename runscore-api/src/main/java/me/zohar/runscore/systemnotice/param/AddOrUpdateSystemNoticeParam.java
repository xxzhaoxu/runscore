package me.zohar.runscore.systemnotice.param;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.systemnotice.domain.SystemNotice;

@Data
public class AddOrUpdateSystemNoticeParam {

	private String id;

	private String title;

	private String content;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date publishTime;

	public SystemNotice convertToPo() {
		SystemNotice po = new SystemNotice();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCreateTime(new Date());
		return po;
	}

}
