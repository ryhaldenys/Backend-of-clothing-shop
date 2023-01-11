package ua.staff.dto;

import ua.staff.model.PostAddress;

import java.math.BigDecimal;

public record PersonDto(Long id, String firstName, String lastName, String numberPhone, BigDecimal bonus, PostAddress address) {
}
