package ua.staff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ua.staff.dto.ClothesDetailsDto;

import ua.staff.model.Clothes;
import ua.staff.model.Image;
import ua.staff.model.Size;

import java.util.Optional;
import java.util.Set;

public interface ClothesRepository extends JpaRepository<Clothes,Long>,CustomClothesRepository {

    @Query("SELECT c.sizes from Clothes c where c.id = ?1")
    Set<Size> findClothesSizesById(Long id);

    @Query("SELECT c.images from Clothes c where c.id = ?1")
    Set<Image> findClothesImagesById(Long id);

    @Query("SELECT new ua.staff.dto.ClothesDetailsDto(c.id,c.name,c.article,c.subType,c.description,c.price,c.discount) from Clothes c where c.id = ?1")
    Optional<ClothesDetailsDto> findClothesById(Long id);

    @Query("select c.sizes from Clothes c join c.sizes s where c.id = ?1 and s.size =?2")
    Optional<Size> findSizeByClothesIdAndSizeType(Long clothesId, String size);

    @Modifying
    @Query(value ="update clothes_sizes cs set amount =?1 where cs.clothes_id=?2 and cs.size =?3" ,nativeQuery = true)
    void updateAmountOfSizes(int amount,long clothes_id,String sizeKind);
}
