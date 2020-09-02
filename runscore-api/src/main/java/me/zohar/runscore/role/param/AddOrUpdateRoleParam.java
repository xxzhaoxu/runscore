package me.zohar.runscore.role.param;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import me.zohar.runscore.common.utils.IdUtils;
import me.zohar.runscore.role.domain.JbRole;

import org.springframework.beans.BeanUtils;

@Data
public class AddOrUpdateRoleParam {

	/**
	 * 主键id
	 */
	private String id;

	@NotBlank
	private String name;

	/**
	 * 备注
	 */
	private String description;

	public JbRole convertToPo() {
		JbRole po = new JbRole();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		return po;
	}

}
