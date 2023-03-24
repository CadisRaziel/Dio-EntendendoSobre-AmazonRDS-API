package com.example.dioclass.apirest.Hateoas.Repositories;


import com.example.dioclass.apirest.Hateoas.Entitys.EmployeeHateoas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepositoryHateoas extends JpaRepository<EmployeeHateoas, Long> {

}
