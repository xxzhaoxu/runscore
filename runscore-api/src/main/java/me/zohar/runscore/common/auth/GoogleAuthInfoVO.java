package me.zohar.runscore.common.auth;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class GoogleAuthInfoVO {

	private String googleSecretKey;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date googleAuthBindTime;

	public static GoogleAuthInfoVO convertFor(String googleSecretKey, Date googleAuthBindTime) {
		GoogleAuthInfoVO vo = new GoogleAuthInfoVO();
		vo.setGoogleSecretKey(googleSecretKey);
		vo.setGoogleAuthBindTime(googleAuthBindTime);
		return vo;
	}

}
