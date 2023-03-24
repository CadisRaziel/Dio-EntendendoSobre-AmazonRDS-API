package com.example.dioclass.apirest.ApiRest;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {
    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/employees")
    //definindo um label para o getMapping
    //criar metodo do tipo lista de employees
    public List<Employee> listOfEmployeeAll(){
        return repository.findAll();
    }

    //metodo que vai retornar a execçao personalizada que criamos
    @GetMapping("/employees/{id}")
    public Employee consultById(@PathVariable Long id){
        //orElseThrow -> estamos tratando caso o id seja nulo ou nao exista
        //com a nossa classe personalizada de exception
        return repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }


    @PostMapping("/employees")
    //RequestBody -> o que esta passando por parametro vai ser enviado dentro do meu body na request
    public Employee newEmployee(@RequestBody Employee newEmployee){
        return repository.save(newEmployee);
    }

    //modificação parcial
    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, Long id) {
        //procuro pelo id e mapeio ele e vejo o que sera atualizado
        return repository.findById(id).map(employee -> {
            employee.setName(newEmployee.getName());
            employee.setAdress(newEmployee.getAdress());
            employee.setRole(newEmployee.getRole());
            return repository.save(newEmployee);
        }).orElseGet(() -> { //orElseGet -> se eu nao encontrei aquele id quer dizer que nao existe
            //então eu só salvo o novo Id que estou recebendo
            //ele nao tem o map porque é um id novo
           newEmployee.setId(id);
           return repository.save(newEmployee);
        });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable long id){
        repository.deleteById(id);
    }

}
