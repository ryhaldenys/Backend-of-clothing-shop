package ua.staff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.staff.builder.UriBuilder;
import ua.staff.dto.AllOrdersDto;
import ua.staff.dto.OrderDto;
import ua.staff.repository.OrderRepository;
import ua.staff.service.OrderService;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @GetMapping
    public Slice<AllOrdersDto> getAll(){
        return orderRepository.findAllOrders();
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }

    @PatchMapping("/{id}") //do not return body
    public ResponseEntity<Void> setStatusReceived(@PathVariable Long id){
        orderService.setStatusReceivedByOrderId(id);

        var location = UriBuilder.createUriFromCurrentRequest();
        return ResponseEntity.status(NO_CONTENT)
                .location(location)
                .build();
    }

    //todo: create method getOrder by orderNumber;
    //todo: add cache to basket and history of orders

}
