package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.util.EmployeeNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class AwardsService {
    private final EmployeeRepository employeeRepository;
    private final AwardEventService awardEventService;
    private final AwardsCache awardsCache;

    public AwardsService(EmployeeRepository employeeRepository, AwardEventService awardEventService, AwardsCache awardsCache) {
        this.employeeRepository = employeeRepository;
        this.awardEventService = awardEventService;
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
        awardEventService.sendEvent(activity, orgId, orgEmployees.size());

        return activity;
    }
}
