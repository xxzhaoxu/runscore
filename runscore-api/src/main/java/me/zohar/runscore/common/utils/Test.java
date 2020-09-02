package me.zohar.runscore.common.utils;

import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("111");
		list.add("222");
		list.add("333");
		List<String> listTemp = new ArrayList<String>();
		listTemp.add("222");
		//listTemp.add("333");


		for (String gatheringCodeId : listTemp) {
			for (int i = 0; i < list.size(); i++) {
				if(gatheringCodeId.equals(list.get(i))){
					list.remove(i);
				}
			}
		}
		System.out.println(list.size());
	}
}
