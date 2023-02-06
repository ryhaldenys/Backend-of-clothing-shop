package ua.staff.service;

import org.springframework.data.domain.Slice;
import ua.staff.dto.AllOrdersDto;
import ua.staff.dto.OrderDto;
import ua.staff.model.Delivery;

public interface OrderService {
    Slice<AllOrdersDto> getAllOrdersByPersonId(Long personId);
    void createOrder(Long personId, Delivery delivery);
    OrderDto getOrderById(Long orderId);
    void setStatusCanceledByOrderId(Long personId, Long orderId);
    void setStatusReceivedByOrderId(Long personId, Long orderId);
    Slice<AllOrdersDto> getAllOrders();
}
