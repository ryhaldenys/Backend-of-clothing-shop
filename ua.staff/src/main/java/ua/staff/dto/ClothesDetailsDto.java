package ua.staff.dto;

import java.math.BigDecimal;

public record ClothesDetailsDto(Long id, String name, String article, String subType,
                                String description, BigDecimal price, BigDecimal discount) {
}
