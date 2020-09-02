package me.zohar.runscore.useraccount.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import me.zohar.runscore.useraccount.domain.OperLog;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class OperLogVO {

	private String id;

	private String system;

	/**
	 * 模块
	 */
	private String module;

	/**
	 * 操作类型
	 */
	private String operate;

	/**
	 * 请求方式:get/post
	 */
	private String requestMethod;

	/**
	 *
	 */
	private String requestUrl;

	private String requestParam;

	private String ipAddr;

	private String operAccountId;

	private String operName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date operTime;

	public static List<OperLogVO> convertFor(List<OperLog> operLogs) {
		if (CollectionUtil.isEmpty(operLogs)) {
			return new ArrayList<>();
		}
		List<OperLogVO> vos = new ArrayList<>();
		for (OperLog operLog : operLogs) {
			vos.add(convertFor(operLog));
		}
		return vos;
	}

	public static OperLogVO convertFor(OperLog operLog) {
		if (operLog == null) {
			return null;
		}
		OperLogVO vo = new OperLogVO();
		BeanUtils.copyProperties(operLog, vo);
		return vo;
	}

}
