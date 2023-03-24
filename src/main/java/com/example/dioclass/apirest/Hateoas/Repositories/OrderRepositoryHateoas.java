package com.example.dioclass.apirest.Hateoas.Repositories;


import com.example.dioclass.apirest.Hateoas.Entitys.OrderHateoas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositoryHateoas extends JpaRepository<OrderHateoas, Long> {
}