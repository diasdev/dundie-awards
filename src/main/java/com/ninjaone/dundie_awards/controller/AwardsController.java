package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.model.Activity;
import com.ninjaone.dundie_awards.service.AwardsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AwardsController {
    private final AwardsService awardsService;
    public AwardsController(AwardsService awardsService) {
        this.awardsService = awardsService;
    }

    @PostMapping("/give-dundie-awards/{organizationId}")
    public ResponseEntity<Activity> awardEmployee(@PathVariable long organizationId) {
        return ResponseEntity.ok(awardsService.giveAward(organizationId));
    }
}
