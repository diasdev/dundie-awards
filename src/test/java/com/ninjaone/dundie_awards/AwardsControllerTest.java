package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.controller.AwardsController;
import com.ninjaone.dundie_awards.service.AwardsService;
import com.ninjaone.dundie_awards.util.EmployeeNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AwardsController.class)
public class AwardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AwardsService awardsService;

    @Test
    public void AwardsController_GiveAwardsToEmptyOrg_ReturnNotFound() throws Exception {
        when(awardsService.giveAward(1)).thenThrow(new EmployeeNotFoundException("No employees of organization with id 1 were found"));

        mockMvc.perform(post("/give-dundie-awards/1"))
                .andExpect(status().isNotFound());
    }
}
