package com.example.dioclass.apirest.ApiRest;


public class EmployeeNotFoundException extends RuntimeException{
    public EmployeeNotFoundException(Long id) {
        super("Could not find the id: " + id);
    }
}
