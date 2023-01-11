package ua.staff.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import ua.staff.model.Clothes;

public interface CustomClothesRepository {
    Slice<Clothes> findClothesJoinFetchImagesAndSizes(String sex,Pageable pageable);
}
