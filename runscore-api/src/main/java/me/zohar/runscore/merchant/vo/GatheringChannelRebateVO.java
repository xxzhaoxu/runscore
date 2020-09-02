package me.zohar.runscore.merchant.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.runscore.merchant.domain.GatheringChannelRebate;

@Data
public class GatheringChannelRebateVO {

	private String id;

	/**
	 * 返点
	 */
	private Double rebate;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private String channelId;

	private String channelCode;

	public static List<GatheringChannelRebateVO> convertFor(List<GatheringChannelRebate> rebates) {
		if (CollectionUtil.isEmpty(rebates)) {
			return new ArrayList<>();
		}
		List<GatheringChannelRebateVO> vos = new ArrayList<>();
		for (GatheringChannelRebate rebate : rebates) {
			vos.add(convertFor(rebate));
		}
		return vos;
	}

	public static GatheringChannelRebateVO convertFor(GatheringChannelRebate rebate) {
		if (rebate == null) {
			return null;
		}
		GatheringChannelRebateVO vo = new GatheringChannelRebateVO();
		BeanUtils.copyProperties(rebate, vo);
		vo.setChannelCode(rebate.getChannel().getChannelCode());
		return vo;
	}

}
