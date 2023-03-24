package com.example.dioclass.apirest.ApiRest;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class LoadBD {

    private static final Logger log = LoggerFactory.getLogger(LoadBD.class);
    @Bean
    public CommandLineRunner commandLineRunner(EmployeeRepository employeeRepository){
        return args -> {
            log.info("Log of event save user 1: " + employeeRepository.save(new Employee("Vitor", "Pai", "rua sao paulo 918")));
            log.info("Log of event save user 1: " + employeeRepository.save(new Employee("Ariella", "Filha", "jd america")));
        };
    }
}
