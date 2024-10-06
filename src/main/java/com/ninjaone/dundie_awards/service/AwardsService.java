package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.MessageBroker;
import com.ninjaone.dundie_awards.model.*;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.util.EmployeeNotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AwardsService {
    private final EmployeeRepository employeeRepository;
    private final MessageBroker messageBroker;
    private final AwardsCache awardsCache;

    public AwardsService(EmployeeRepository employeeRepository, MessageBroker messageBroker, AwardsCache awardsCache) {
        this.employeeRepository = employeeRepository;
        this.messageBroker = messageBroker;
        this.awardsCache = awardsCache;
    }

    @Transactional
    public Activity giveAward(long orgId) {
        List<Employee> orgEmployees = employeeRepository.findAllByOrganizationId(orgId);

        if (orgEmployees.isEmpty()) {
            throw new EmployeeNotFoundException(String.format("No employees of organization with id %s were found", orgId));
        }

        for (Employee employee : orgEmployees) {
            employee.setDundieAwards(Objects.requireNonNullElse(employee.getDundieAwards(), 0) + 1);
            awardsCache.addOneAward();
        }

        Activity activity = new Activity(LocalDateTime.now(), "Awards given to all employees in organization with id " + orgId);
        sendEvent(activity, orgId, orgEmployees.size());

        return activity;
    }

    private void sendEvent(Activity activity, long orgId, int numberOfAwards) {
        messageBroker.sendMessage(buildMessage(activity, orgId, numberOfAwards));
    }

    private AwardMessage buildMessage(Activity activity, long orgId, int numberOfAwards) {
        return new AwardMessage(activity, buildRollbackData(orgId, numberOfAwards));
    }

    private AwardsRollbackData buildRollbackData(long orgId, int numberOfAwards) {
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        params.put("numberOfAwards", numberOfAwards);

        return new AwardsRollbackData(params);
    }

    @EventListener
    public void handleAwardsRollbackEvent(AwardsRollbackEvent rollbackEvent) {
        processRollbackEvent(rollbackEvent.rollbackData());
    }

    @Transactional
    private void processRollbackEvent(AwardsRollbackData rollbackData) {
        try {
            String updateQuery = "UPDATE Employee e SET e.dundieAwards = e.dundieAwards - 1 WHERE e.organization.id = :orgId";
            Map<String, Object> params = new HashMap<>();
            params.put("orgId", rollbackData.data().get("orgId"));

            // Depending on domain rules regarding award removal, this return (number of rows affected) may be used to decrement the cache
            employeeRepository.executeDynamicCommand(updateQuery, params);

            awardsCache.removeAwards( (int) rollbackData.data().get("numberOfAwards"));
        } catch (Exception e) {
            //TODO handle event in a fullly-implemented queue with dead-letter queue to keep errors from being lost
            throw new RuntimeException("Rollback of original operation failed.", e);
        }
    }
}
