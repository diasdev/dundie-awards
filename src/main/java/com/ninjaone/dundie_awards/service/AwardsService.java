package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public Activity giveAward(long OrgId) {
        for (Employee employee : employeeRepository.findAllByOrganizationId(OrgId)) {
            employee.setDundieAwards(Objects.requireNonNullElse(employee.getDundieAwards(), 0) + 1);
        }

        Activity activity = new Activity(LocalDateTime.now(), "Awards given to all employees in organization with id " + OrgId);

        sendEvent(activity);

        return activity;
    }

    private void sendEvent(Activity activity) {
        messageBroker.sendMessage(activity);
    }
}
