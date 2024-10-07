package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.model.Organization;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;
import com.ninjaone.dundie_awards.service.AwardsEventService;
import com.ninjaone.dundie_awards.service.AwardsService;
import com.ninjaone.dundie_awards.util.EmployeeNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class AwardsServiceTest {

    @Autowired
    private AwardsService awardsService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @MockBean
    private AwardsCache awardsCache;

    @MockBean
    private AwardsEventService awardsEventService;

    @BeforeEach
    public void setUp() {
        Organization organization = organizationRepository.save(new Organization("dunder mifflin"));
        employeeRepository.save(new Employee("Kevin", "Malone", organization));
        employeeRepository.save(new Employee("Toby", "Flenderson", organization));
    }

    @AfterEach
    public void tearDown() {
        employeeRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test
    public void AwardsService_GiveValidAward_IncrementsAwards() {
        Mockito.doNothing().when(awardsCache).addOneAward();
        Mockito.doNothing().when(awardsEventService).sendEvent(Mockito.any(), Mockito.anyLong(), Mockito.anyInt());
        awardsService.giveAward(1);

        List<Employee> employees = employeeRepository.findAllByOrganizationId(1);

        assertTrue(employees.stream().allMatch(e -> e.getDundieAwards() == 1));
    }

    @Test
    public void AwardsService_GiveAwardToEmptyOrg_ThrowsException() {
        assertThrows(EmployeeNotFoundException.class, () -> awardsService.giveAward(0));
    }
}
