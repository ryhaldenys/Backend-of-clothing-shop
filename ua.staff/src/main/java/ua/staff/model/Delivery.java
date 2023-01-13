package ua.staff.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Delivery {

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private BigDecimal deliveryPrice;

    @Column(nullable = false)
    private String deliveryType;

    @Column(nullable = false)
    private String paymentKind;
}
