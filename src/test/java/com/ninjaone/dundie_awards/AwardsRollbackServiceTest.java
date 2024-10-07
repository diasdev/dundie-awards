package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.model.AwardsRollbackEvent;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;
import com.ninjaone.dundie_awards.service.AwardsRollbackService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class AwardsRollbackServiceTest {
    @Autowired
    private AwardsRollbackService awardsRollbackService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @MockBean
    private AwardsCache awardsCache;

    @BeforeEach
    public void setUp() {
        Organization organization = organizationRepository.save(new Organization("dunder mifflin"));
        Employee emp = new Employee("Kevin", "Malone", organization);
        emp.setDundieAwards(2);
        Employee savedEmp = employeeRepository.save(emp);
    }

    @AfterEach
    public void tearDown() {
        employeeRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test
    public void AwardsRollbackService_RollbackAward_DecrementsAwards() {
        Mockito.doNothing().when(awardsCache).removeAwards(ArgumentMatchers.anyInt());
        awardsRollbackService.handleAwardsRollbackEvent(new AwardsRollbackEvent(awardsRollbackService.buildRollbackData(1, 1), "Test"));

        Employee rolledBackEmployee = employeeRepository.findAll().get(0);
        System.out.println(rolledBackEmployee.getId());
        assertEquals(1, rolledBackEmployee.getDundieAwards());
    }
}
