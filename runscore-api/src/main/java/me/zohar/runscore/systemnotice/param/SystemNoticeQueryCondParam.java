package me.zohar.runscore.systemnotice.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.runscore.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class SystemNoticeQueryCondParam extends PageParam {

	private String title;

}
