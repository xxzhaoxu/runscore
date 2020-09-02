package me.zohar.runscore.task.merchant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.zohar.runscore.merchant.service.MerchantOrderService;

@Component
@Slf4j
public class FreezeOrderTask {

	@Autowired
	private MerchantOrderService merchantOrderService;

	@Scheduled(fixedRate = 6000)
	public void execute() {
		try {
			log.info("商户订单冻结定时任务start");
			merchantOrderService.freezeOrder();
			log.info("商户订单冻结定时任务end");
		} catch (Exception e) {
			log.error("商户订单冻结定时任务", e);
		}
	}

}
