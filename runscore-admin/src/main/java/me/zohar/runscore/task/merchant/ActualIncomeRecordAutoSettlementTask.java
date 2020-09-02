package me.zohar.runscore.task.merchant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.zohar.runscore.merchant.service.MerchantOrderService;

@Component
@Slf4j
public class ActualIncomeRecordAutoSettlementTask {

	@Autowired
	private MerchantOrderService merchantOrderService;

	@Scheduled(fixedRate = 30000)
	public void execute() {
		try {
			log.info("实收金额记录自动结算定时任务start");
			merchantOrderService.actualIncomeRecordAutoSettlement();
			log.info("实收金额记录自动结算定时任务end");
		} catch (Exception e) {
			log.error("实收金额记录自动结算定时任务", e);
		}
	}

}
