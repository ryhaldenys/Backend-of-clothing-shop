package ua.staff.dto;

import lombok.Getter;
import lombok.Setter;
import ua.staff.model.Image;

import java.math.BigDecimal;

public record ChoseClothesDto(Long choseClothesId,Integer amountOfClothes,String choseSize,
                                Long clothesId,String name,BigDecimal price,BigDecimal discount,String imageUrl){
}
