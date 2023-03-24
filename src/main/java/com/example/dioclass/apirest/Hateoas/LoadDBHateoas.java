package com.example.dioclass.apirest.Hateoas;

import com.example.dioclass.apirest.Hateoas.Entitys.EmployeeHateoas;
import com.example.dioclass.apirest.Hateoas.Entitys.OrderHateoas;
import com.example.dioclass.apirest.Hateoas.Entitys.Status;
import com.example.dioclass.apirest.Hateoas.Repositories.EmployeeRepositoryHateoas;
import com.example.dioclass.apirest.Hateoas.Repositories.OrderRepositoryHateoas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LoadDBHateoas {

    private static final Logger log = LoggerFactory.getLogger(LoadDBHateoas.class);
    // persistindo dados no banco com jpa

    @Bean
    CommandLineRunner loaddata(EmployeeRepositoryHateoas employeeRepositoryHateoas, OrderRepositoryHateoas orderRepositoryHateoas) {
        return args -> {
            log.info("Log of save event: " + employeeRepositoryHateoas.save(new EmployeeHateoas("Maria Silva", "Chef",
                    "avenida silveira dutra 1002")));
            log.info("log of save event: " + employeeRepositoryHateoas.save(new EmployeeHateoas("John Dutra", "Mecanico",
                    "rua joao freire 231")));
            log.info("Log of save event: " + employeeRepositoryHateoas.save(new EmployeeHateoas("Bilbo Baggins", "thief",
                    "The shine")));
            orderRepositoryHateoas.save(new OrderHateoas(Status.COMPLETED, "review"));
            orderRepositoryHateoas.save(new OrderHateoas(Status.IN_PROGRESS, "travel"));
            orderRepositoryHateoas.save(new OrderHateoas(Status.IN_PROGRESS, "sale"));

            //farei uma requisição ao banco, vou puxar tudo
            //pra cada registro que eu receber eu vou ta printando o log com a função order
            //isso é exatamente a mesma coisa disso: (o que esta ali em cima)
            //log.info("Log of save event: " + employeeRepositoryHateoas.save(new EmployeeHateoas("Bilbo Baggins", "thief",
            //       "The shine")));
            orderRepositoryHateoas.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });
        };
    }
}