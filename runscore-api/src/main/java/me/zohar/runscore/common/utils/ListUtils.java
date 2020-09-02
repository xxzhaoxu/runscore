package me.zohar.runscore.common.utils;

import java.util.ArrayList;
import java.util.List;

import me.zohar.runscore.merchant.vo.MyWaitConfirmOrderVO;

public class ListUtils {

	//去重
	public static List<String> duplicateRemoval(List<MyWaitConfirmOrderVO> waitConfirmOrders){
		List<String> listTemp = new ArrayList<String>();
		for(int i=0;i<waitConfirmOrders.size();i++){
            if(!listTemp.contains(waitConfirmOrders.get(i).getGatheringCodeId())){
            	listTemp.add(waitConfirmOrders.get(i).getGatheringCodeId());
            }
        }
		/*for (String string : listTemp) {
			System.out.println("收款码ID："+string);
		}*/
		return listTemp;
	}

	//去重
	public static List<MyWaitConfirmOrderVO> duplicateRemovalList(List<MyWaitConfirmOrderVO> waitConfirmOrders){
		List<MyWaitConfirmOrderVO> listTemp = new ArrayList<MyWaitConfirmOrderVO>();
		for(int i=0;i<waitConfirmOrders.size();i++){
			if(!listTemp.contains(waitConfirmOrders.get(i).getGatheringCodeId())){
				listTemp.add(waitConfirmOrders.get(i));
			}
		}
		/*for (String string : listTemp) {
			System.out.println("收款码ID："+string);
		}*/
		return listTemp;
	}
}
