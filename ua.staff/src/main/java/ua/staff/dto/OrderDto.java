package ua.staff.dto;

import lombok.Getter;
import lombok.Setter;
import ua.staff.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private String orderNumber;
    private BigDecimal totalPrice;
    private Order.Status status;
    private BigDecimal usedBonuses;
    private LocalDateTime createdAt;
    private List<ChoseClothesDto> choseClothes;

    public OrderDto(Long id, String orderNumber,
                    BigDecimal totalPrice, Order.Status status, BigDecimal usedBonuses, LocalDateTime createdAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.totalPrice = totalPrice;
        this.status = status;
        this.usedBonuses = usedBonuses;
        this.createdAt = createdAt;
    }
}
