package com.example.dioclass.apirest.ApiRest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
