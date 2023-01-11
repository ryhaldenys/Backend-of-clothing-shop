package ua.staff.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
public class BasketDto {
    private Long id;
    private BigDecimal usedBonuses;
    private List<ChoseClothesDto> clothes;

    public BasketDto(Long id, BigDecimal usedBonuses) {
        this.id = id;
        this.usedBonuses = usedBonuses;
    }
}

