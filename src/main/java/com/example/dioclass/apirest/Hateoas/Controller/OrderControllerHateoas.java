package com.example.dioclass.apirest.Hateoas.Controller;


import com.example.dioclass.apirest.Hateoas.Entitys.OrderHateoas;
import com.example.dioclass.apirest.Hateoas.Entitys.Status;
import com.example.dioclass.apirest.Hateoas.Exceptions.OrderNotFoundExceptionHateoas;
import com.example.dioclass.apirest.Hateoas.Repositories.OrderRepositoryHateoas;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderControllerHateoas {

    //definindo o repository
    private final OrderRepositoryHateoas repositoryOrder;

    public OrderControllerHateoas(OrderRepositoryHateoas repositoryHateoas) {
        this.repositoryOrder = repositoryHateoas;
    }

    @GetMapping("/orders")
    //ResponseEntity -> Classe especifica que deixa adicionar uma lista
    //para adicionar um link a cada registro pra retornar ao usuario
    ResponseEntity<List<OrderHateoas>> consultOrderAll() {
        long idOrder;
        Link linkUri;
        List<OrderHateoas> orderList = repositoryOrder.findAll();
        if (orderList.isEmpty()) {
            //se a lista tive fazia, retornarmos um response error not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //caso a lista não estiver vazia
        //ele vai varrer a lista e tirar um a um do que foi recebido
        for (OrderHateoas orderHateoas : orderList){
            //id para gerar o link
            idOrder = orderHateoas.getId();

            //definindo o link
            //linkTo -> cria a uri
            //methodOn -> associa essa uri a um metodo especifico que vamos determinar (passamso o controller)
            //withSelfRel -> relação que o link tem com o metodo
            linkUri = linkTo(methodOn(OrderControllerHateoas.class).consultOneOrder(idOrder)).withSelfRel();
            //adicionando o link ao hateaos
            //quando eu enviar o meu ResponseEntity eu vou ta enviando juntamente esse link
            //e ele vai ser criada de forma dinamica, toda vez que for feita uma consulta no banco,
            //vai ser criado um link dinamicamente (vai refletir as atualizações do banco)
            orderHateoas.add(linkUri);

            //withRel -> link de proposito geral
            //toda vez que for realizada uma consulta com esse get '@GetMapping("/orders")'
            //eu vou ta retornando para ele esse link abaixo
            linkUri = linkTo(methodOn(OrderControllerHateoas.class).consultOrderAll()).withRel("Custumer order list");
            orderHateoas.add(linkUri);
        }
        return new ResponseEntity<List<OrderHateoas>>(orderList, HttpStatus.OK);

    }

    @GetMapping("/orders/{id}")
    ResponseEntity<OrderHateoas> consultOneOrder(@PathVariable Long id) {
        //Optional -> caso o ponteiro nao existir
        Optional<OrderHateoas> orderPointer = repositoryOrder.findById(id);
        if (orderPointer.isPresent()) { //isPresent -> se o ponteiro esta presente

            //vamos crair uma ordem com o hateaos
            //com isso consigo pega informação do meu ponteiro(Optional)
            OrderHateoas order = orderPointer.get();

            //adicionando o link a uri
            order.add(linkTo(methodOn(OrderControllerHateoas.class).consultOrderAll()).withRel("All orders"));
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //adicionando um employee
    @PostMapping("/orders")
    OrderHateoas newOrder(@RequestBody OrderHateoas newOrder) {
        return repositoryOrder.save(newOrder);
    }

    //modificação parcial
    @PutMapping("/orders/{id}")
    OrderHateoas replaceOrder(@RequestBody OrderHateoas newOrder, long id) {
        return repositoryOrder.findById(id).map(order -> {
            order.setDescription(newOrder.getDescription());
            order.setStatus(newOrder.getStatus());
            return repositoryOrder.save(newOrder);
        }).orElseGet(() -> {
            newOrder.setId(id);
            return repositoryOrder.save(newOrder);
        });
    }

    @DeleteMapping("/orders/{id}")
    void deleteOrder(@PathVariable long id) {
        repositoryOrder.deleteById(id);
    }

    @PutMapping("/orders/{id}/cancel")
    public ResponseEntity<?> cancelOrderById(@PathVariable long id){
        OrderHateoas cancelledOrder = repositoryOrder.findById(id).orElseThrow(
                () -> new OrderNotFoundExceptionHateoas(id));
        if (cancelledOrder.getStatus() == Status.IN_PROGRESS){
            cancelledOrder.setStatus(Status.CANCELLED);
            cancelledOrder.add(linkTo(methodOn(OrderControllerHateoas.class).consultOneOrder(id)).withSelfRel());
            cancelledOrder.add(linkTo(methodOn(OrderControllerHateoas.class).consultOrderAll())
                    .withRel("Complete order list"));
            repositoryOrder.save(cancelledOrder);
            return new ResponseEntity<>(cancelledOrder,HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE,
                        MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body("You can't complete the task, the order has a " +
                        cancelledOrder.getStatus() + " status");
    }

    @PutMapping("/orders/{id}/complete")
    public ResponseEntity<?> completeOrderById(@PathVariable long id){
        OrderHateoas cancelledOrder = repositoryOrder.findById(id).orElseThrow(
                () -> new OrderNotFoundExceptionHateoas(id));
        if (cancelledOrder.getStatus() == Status.IN_PROGRESS){
            cancelledOrder.setStatus(Status.COMPLETED);
            cancelledOrder.add(linkTo(methodOn(OrderControllerHateoas.class).consultOneOrder(id)).withSelfRel());
            cancelledOrder.add(linkTo(methodOn(OrderControllerHateoas.class).consultOrderAll())
                    .withRel("Complete order list"));
            repositoryOrder.save(cancelledOrder);
            return new ResponseEntity<>(cancelledOrder,HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED) //
                .header(HttpHeaders.CONTENT_TYPE,
                        MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE) //
                .body("You can't complete the task, the order has a " +
                        cancelledOrder.getStatus() + " status");
    }

}