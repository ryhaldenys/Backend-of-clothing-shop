package ua.staff.dto;

import ua.staff.model.Role;

import java.util.List;

public record PersonRolesDto(PersonDto person, List<Role> roles) {
}
