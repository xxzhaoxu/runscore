package me.zohar.runscore.task.merchant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import me.zohar.runscore.useraccount.service.LoginLogService;

@Component
@Slf4j
public class UpdateReceiveOrderStateToStopTask {

	@Autowired
	private LoginLogService loginLogService;

	@Scheduled(fixedRate = 20000)
	public void execute() {
		try {
			log.info("定时更新状态为停止接单start");
			loginLogService.updateReceiveOrderStateToStop();
			log.info("定时更新状态为停止接单end");
		} catch (Exception e) {
			log.error("定时更新状态为停止接单", e);
		}
	}

}
