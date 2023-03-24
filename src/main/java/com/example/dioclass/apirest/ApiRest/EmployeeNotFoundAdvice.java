package com.example.dioclass.apirest.ApiRest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice //-> para ser gerenciado pelo string
public class EmployeeNotFoundAdvice {
    //envia a exception via response(no body)
    //se o front tratar o erro corretamente, essa classe vai enviar a msg da classe EmployeeNotFoundException
    @ResponseBody
    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(EmployeeNotFoundException ex){
        final String message = ex.getMessage();
        return message;
    }
}
