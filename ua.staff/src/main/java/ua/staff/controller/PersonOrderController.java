package ua.staff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.staff.builder.UriBuilder;
import ua.staff.dto.AllOrdersDto;
import ua.staff.dto.OrderDto;
import ua.staff.model.Delivery;
import ua.staff.repository.OrderRepository;
import ua.staff.service.OrderService;


@RestController
@RequestMapping("/people/{person_id}/orders")
@RequiredArgsConstructor
public class PersonOrderController {
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @GetMapping
    public Slice<AllOrdersDto> getAllOrders(@PathVariable Long person_id){
        return orderRepository.findAllByPersonId(person_id);
    }

    @GetMapping("/{order_id}")
    public OrderDto getOrder(@PathVariable("order_id")Long orderId){
        return orderService.getOrderById(orderId);
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@PathVariable Long person_id, @RequestBody Delivery delivery){
        orderService.createOrder(person_id,delivery);
        var location = UriBuilder.createUriFromCurrentRequest();

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .build();
    }

    @PatchMapping("/{order_id}")
    public void setStatusCanceled(@PathVariable("order_id")Long orderId){
        orderService.setStatusCanceledByOrderId(orderId);
    }

}
