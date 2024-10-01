package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.model.Employee;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AwardController {
    private final EmployeeRepository employeeRepository;
    public AwardController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/award/{OrgId}")
    public ResponseEntity<List<Employee>> awardEmployee(@PathVariable long OrgId) {
        return ResponseEntity.ok(employeeRepository.findAllByOrganizationId(OrgId));
    }
}
