package ua.staff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.staff.builder.UriBuilder;
import ua.staff.dto.AllOrdersDto;
import ua.staff.dto.OrderDto;
import ua.staff.model.Delivery;
import ua.staff.service.OrderService;

import static org.springframework.http.HttpStatus.CREATED;
import static ua.staff.generator.ResponseEntityGenerator.getResponseEntity;
import static ua.staff.generator.ResponseEntityGenerator.getResponseEntityWithNoContent;

@RestController
@RequestMapping("/api/people/{person_id}/orders")
@RequiredArgsConstructor

public class PersonOrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAuthority('advanced') or authentication.principal.id == #id")
    public Slice<AllOrdersDto> getAllOrders(@PathVariable("person_id") Long id){
        return orderService.getAllOrdersByPersonId(id);
    }

    @GetMapping("/{order_id}")
    @PreAuthorize("hasAuthority('advanced') or authentication.principal.id == #id")
    public OrderDto getOrder(@PathVariable("person_id") Long id,@PathVariable("order_id")Long orderId){
        return orderService.getOrderById(orderId);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('advanced') or authentication.principal.id == #id")
    public ResponseEntity<Void> createOrder(@PathVariable("person_id") Long id, @RequestBody Delivery delivery){
        orderService.createOrder(id,delivery);
        var location = UriBuilder.createUriFromCurrentRequest();
        return getResponseEntity(location, CREATED);
    }

    @PatchMapping("/{order_id}/canceled")
    @PreAuthorize("hasAuthority('advanced') or authentication.principal.id == #id")
    public void setStatusCanceled(@PathVariable("person_id") Long id,@PathVariable("order_id") Long orderId){
        orderService.setStatusCanceledByOrderId(id,orderId);
    }

    @PatchMapping("/{order_id}/received")
    @PreAuthorize("hasAuthority('advanced')")
    public ResponseEntity<Void> setStatusReceived(@PathVariable("person_id") Long id,@PathVariable("order_id") Long orderId){
        orderService.setStatusReceivedByOrderId(id,orderId);

        var location = UriBuilder.createUriFromCurrentRequest();
        return getResponseEntityWithNoContent(location);
    }

}
