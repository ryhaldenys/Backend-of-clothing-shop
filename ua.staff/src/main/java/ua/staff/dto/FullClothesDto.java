package ua.staff.dto;

import ua.staff.model.Image;
import ua.staff.model.Size;

import java.util.Set;

public record FullClothesDto(ClothesDetailsDto clothesDetails, Set<Image> images, Set<Size>sizes) {
}
