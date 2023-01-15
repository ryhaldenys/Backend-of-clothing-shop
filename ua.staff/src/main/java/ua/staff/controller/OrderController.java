package ua.staff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import ua.staff.dto.AllOrdersDto;
import ua.staff.dto.OrderDto;
import ua.staff.repository.OrderRepository;
import ua.staff.service.OrderService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @GetMapping
    @Cacheable("all_orders")
    public Slice<AllOrdersDto> getAll(){
        return orderRepository.findAllOrders();
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }

    //todo: create method getOrder by orderNumber;
    //todo: add cache to basket and history of orders

}
