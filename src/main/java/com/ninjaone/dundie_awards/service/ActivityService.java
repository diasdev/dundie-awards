package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.AwardMessage;
import com.ninjaone.dundie_awards.model.RollbackCommand;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    private final ActivityRepository activityRepository;
    private final EmployeeRepository employeeRepository;
    private final MessageBroker messageBroker;

    public ActivityService(ActivityRepository activityRepository, EmployeeRepository employeeRepository, MessageBroker messageBroker) {
        this.activityRepository = activityRepository;
        this.messageBroker = messageBroker;
        this.employeeRepository = employeeRepository;
    }

    @Async
    @Scheduled(fixedRate = 5_000L)
    public void pollQueue() {
        AwardMessage message = messageBroker.getMessage();
        if (message != null) {
            processMessage(message);
        }
        System.out.println(message);
    }

    private void processMessage(AwardMessage message) {
        try {
            activityRepository.save(message.activity());
            logger.info("Message processed");
        } catch (Exception e) {
            logger.error("Exception occurred. Rollback of original operation will be perform.", e);
            executeRollbackCommand(message.rollbackCommand());
        }
    }

    private void executeRollbackCommand(RollbackCommand rollbackCommand) {
        employeeRepository.executeDynamicCommand(rollbackCommand.command(), rollbackCommand.params());
        logger.info("Message rolled back");
    }
}
