package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.model.AwardMessage;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.RollbackCommand;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AwardsService {
    private final EmployeeRepository employeeRepository;
    private final MessageBroker messageBroker;

    public AwardsService(EmployeeRepository employeeRepository, MessageBroker messageBroker) {
        this.employeeRepository = employeeRepository;
        this.messageBroker = messageBroker;
    }

    @Transactional
    public Activity giveAward(long orgId) {
        for (Employee employee : employeeRepository.findAllByOrganizationId(orgId)) {
            employee.setDundieAwards(Objects.requireNonNullElse(employee.getDundieAwards(), 0) + 1);
        }

        Activity activity = new Activity(LocalDateTime.now(), "Awards given to all employees in organization with id " + orgId);

        sendEvent(activity, orgId);

        return activity;
    }

    private void sendEvent(Activity activity, long orgId) {
        messageBroker.sendMessage(buildMessage(activity, orgId));
    }

    private AwardMessage buildMessage(Activity activity, long orgId) {
        return new AwardMessage(activity, buildRollbackCommand(orgId));
    }

    private RollbackCommand buildRollbackCommand(long orgId) {
        String updateQuery = "UPDATE Employee e SET e.dundieAwards = e.dundieAwards - 1 WHERE e.organization.id = :orgId";
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);

        return new RollbackCommand(updateQuery, params);
    }
}
