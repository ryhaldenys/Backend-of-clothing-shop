package ua.staff.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import ua.staff.model.Clothes;

public interface CustomClothesRepository {
    Slice<Clothes> findClothesFetchImagesAndSizesBySex(String sex, Pageable pageable);
    Slice<Clothes> findClothesFetchImagesAndSizesBySexAndType(String sex, String type, Pageable pageable);
    Slice<Clothes> findClothesFetchImagesAndSizesBySexAndSubType(String sex, String subtype, Pageable pageable);
}
