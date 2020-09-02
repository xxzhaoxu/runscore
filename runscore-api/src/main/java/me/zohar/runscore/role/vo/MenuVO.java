package me.zohar.runscore.role.vo;

import java.util.List;

import lombok.Data;
import me.zohar.runscore.role.domain.JbPermission;

@Data
public class MenuVO {

	/** 主键 */
	private String id;

	/** 标题 */
	private String title;

	/** 地址 */
	private String url;

	/** 权限编码 */
	private String permCode;

	private List<JbPermission> subMenus;

}
