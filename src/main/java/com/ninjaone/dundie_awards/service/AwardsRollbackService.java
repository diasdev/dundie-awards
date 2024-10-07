package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.AwardsCache;
import com.ninjaone.dundie_awards.model.AwardsRollbackData;
import com.ninjaone.dundie_awards.model.AwardsRollbackEvent;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
public class AwardsRollbackService {
    private final EmployeeRepository employeeRepository;
    private final AwardsCache awardsCache;

    public AwardsRollbackService(EmployeeRepository employeeRepository, AwardsCache awardsCache) {
        this.employeeRepository = employeeRepository;
        this.awardsCache = awardsCache;
    }

    public AwardsRollbackData buildRollbackData(long orgId, int numberOfAwards) {
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
