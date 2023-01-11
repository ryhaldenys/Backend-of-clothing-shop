package ua.staff.dto;

import java.math.BigDecimal;

public record ClothesDto(Long id, String name, BigDecimal price, String pictureUrl, String sizes) {

}
