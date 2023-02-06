package ua.staff.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import ua.staff.dto.ClothesDto;
import ua.staff.dto.FullClothesDto;
import ua.staff.model.Clothes;
import ua.staff.model.ClothesRequestParams;
import ua.staff.model.Image;
import ua.staff.model.Size;

public interface ClothesService {
    Slice<ClothesDto> getClothesDtos(ClothesRequestParams params, Pageable pageable);
    FullClothesDto getClothesById(Long id);
    Clothes saveClothes(Clothes clothes);
    void deleteClothesById(Long id);
    void addImage(Long id, Image image);
    void removeImage(Long id,Image image);
    void addSize(Long id, Size size);
    void removeSize(Long id,Size size);
}
