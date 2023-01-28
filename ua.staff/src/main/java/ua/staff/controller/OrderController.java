package ua.staff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.staff.dto.AllOrdersDto;
import ua.staff.dto.OrderDto;
import ua.staff.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @Cacheable("all_orders")
    @PreAuthorize("hasAuthority('advanced')")
    public Slice<AllOrdersDto> getAll(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('advanced')")
    public OrderDto getOrderById(@PathVariable Long id){
        return orderService.getOrderById(id);
    }

    //todo: create method getOrder by orderNumber;
}
