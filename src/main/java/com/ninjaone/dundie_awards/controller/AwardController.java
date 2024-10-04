package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.service.AwardsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AwardController {
    private final AwardsService awardsService;
    public AwardController(AwardsService awardsService) {
        this.awardsService = awardsService;
    }

    @PostMapping("/award/{OrgId}")
    public ResponseEntity<Activity> awardEmployee(@PathVariable long OrgId) {
        return ResponseEntity.ok(awardsService.giveAward(OrgId));
    }
}
