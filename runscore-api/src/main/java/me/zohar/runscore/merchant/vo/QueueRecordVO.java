package me.zohar.runscore.merchant.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import me.zohar.runscore.merchant.domain.QueueRecord;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 实时排队记录vo
 *
 * @author zohar
 * @date 2019年6月29日
 *
 */
@Data
public class QueueRecordVO {

	/**
	 * 主键id
	 */
	private String id;

	private Double cashDeposit;

	private String userAccountId;

	private String userName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date queueTime;

	public static List<QueueRecordVO> convertFor(List<QueueRecord> queueRecord) {
		if (CollectionUtil.isEmpty(queueRecord)) {
			return new ArrayList<>();
		}
		List<QueueRecordVO> vos = new ArrayList<>();
		for (QueueRecord platformOrder : queueRecord) {
			vos.add(convertFor(platformOrder));
		}
		return vos;
	}

	public static QueueRecordVO convertFor(QueueRecord queueRecord) {
		if (queueRecord == null) {
			return null;
		}
		QueueRecordVO vo = new QueueRecordVO();
		BeanUtils.copyProperties(queueRecord, vo);
		if (StrUtil.isNotBlank(queueRecord.getUserAccountId()) && queueRecord.getUserAccount() != null) {
			vo.setUserName(queueRecord.getUserAccount().getUserName());
			vo.setUserAccountId(queueRecord.getUserAccount().getId());
			vo.setCashDeposit(queueRecord.getUserAccount().getCashDeposit());
		}
		return vo;
	}

}
