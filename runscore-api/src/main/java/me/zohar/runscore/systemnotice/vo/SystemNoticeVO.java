package me.zohar.runscore.systemnotice.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.systemnotice.domain.SystemNotice;

@Data
public class SystemNoticeVO implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String id;

	private String title;

	private String content;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Date publishTime;

	public static List<SystemNoticeVO> convertFor(List<SystemNotice> systemNotices) {
		if (CollectionUtil.isEmpty(systemNotices)) {
			return new ArrayList<>();
		}
		List<SystemNoticeVO> vos = new ArrayList<>();
		for (SystemNotice systemNotice : systemNotices) {
			vos.add(convertFor(systemNotice));
		}
		return vos;
	}

	public static SystemNoticeVO convertFor(SystemNotice systemNotice) {
		if (systemNotice == null) {
			return null;
		}
		SystemNoticeVO vo = new SystemNoticeVO();
		BeanUtils.copyProperties(systemNotice, vo);
		return vo;
	}

}
