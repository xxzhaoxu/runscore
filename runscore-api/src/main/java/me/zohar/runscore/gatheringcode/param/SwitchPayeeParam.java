package me.zohar.runscore.gatheringcode.param;

import java.util.List;

import lombok.Data;

@Data
public class SwitchPayeeParam {

	private List<String> payees;

	private List<String> receiveOrderChannels;

	private String userAccountId;

}
