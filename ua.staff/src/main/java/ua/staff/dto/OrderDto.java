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
    private String personFullName;
    private String address;
    private String deliveryType;
    private BigDecimal deliveryPrice;
    private String paymentKind;

    private List<ChoseClothesDto> choseClothes;

    public OrderDto(Long id, String orderNumber, BigDecimal totalPrice,
                    Order.Status status, BigDecimal usedBonuses, LocalDateTime createdAt,
                    String personName, String address, String deliveryType,
                    BigDecimal deliveryPrice, String paymentKind) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.totalPrice = totalPrice;
        this.status = status;
        this.usedBonuses = usedBonuses;
        this.createdAt = createdAt;
        this.personFullName = personName;
        this.address = address;
        this.deliveryType = deliveryType;
        this.deliveryPrice = deliveryPrice;
        this.paymentKind = paymentKind;
    }
}
