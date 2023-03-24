package com.example.dioclass.apirest;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {
    private final PersonRepository repositoryPerson;

    public PersonController(PersonRepository personRepository) {
        this.repositoryPerson = personRepository;
    }

    //aqui dentro estão dispostos o metodo de requisição

    @GetMapping("/persons")
    public List<Person> consultAllPersons(){
        return repositoryPerson.findAll();
    }

    @GetMapping("/persons/{id}")
    //Optional -> O parametro é passado por ponteiro(a partir do momento que eu tento encontrar uma informação e ela nao existe
    //trata o problema de ponteiro inexistente
    //nao vai transmitir nenhum erro pro usuario
    //e nao vai da nenhum problema na nossa aplicação
    public Optional<Person> consultById(@PathVariable Long id){
        //return repositoryPerson.findById(id).orElseThrow(); //-> para tratar caso o id esteja nullo ou inexistente(retorna execeção)
        return repositoryPerson.findById(id);
    }

    @GetMapping("/")
    public String helloWorld(){
        return "This is my first api in spring boot";
    }
}
