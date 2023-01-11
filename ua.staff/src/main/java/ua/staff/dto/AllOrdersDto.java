package ua.staff.dto;

import ua.staff.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AllOrdersDto(Long id, String orderNumber, BigDecimal totalPrice, BigDecimal usedBonuses, LocalDateTime created_at,
                           Order.Status status) {
}
