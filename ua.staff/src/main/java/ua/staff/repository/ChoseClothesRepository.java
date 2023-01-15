package ua.staff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.staff.dto.ChoseClothesDto;
import ua.staff.model.ChoseClothes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ChoseClothesRepository extends JpaRepository<ChoseClothes,Long> {

    @Query("select c from ChoseClothes c join fetch c.clothes where c.id = ?1 and c.basket.id = ?2")
    Optional<ChoseClothes> findByIdAndPersonId(Long id, Long person_id);

    @Query("select new ua.staff.dto.ChoseClothesDto(c.id,c.amountOfClothes,c.sizeKind," +
            "cl.id,c.clothes.name,c.clothes.price,c.clothes.discount,i.url) "+
            "from ChoseClothes c join c.clothes cl join cl.images i " +
            "where c.basket.id =?1 and i.isMain = true")
    List<ChoseClothesDto> findAllByBasketId(Long basketId);



    @Query("select new ua.staff.dto.ChoseClothesDto(c.id,c.amountOfClothes,c.sizeKind," +
            "cl.id,c.clothes.name,c.clothes.price,c.clothes.discount,i.url) "+
            "from ChoseClothes c join c.clothes cl join cl.images i " +
            "where c.order.id =?1 and i.isMain = true")
    List<ChoseClothesDto> findAllByOrderId(Long orderId);

    @Query("select sum(c.amountOfClothes*(cl.price - (cl.price*cl.discount))) as totalPrice from ChoseClothes c join c.clothes cl where c.basket.id = ?1")
    Optional<BigDecimal> findTotalPriceOfChoseClothesByBasketId(Long basketId);

    @Query("select sum(cc.amountOfClothes*(cl.price - (cl.price*cl.discount))) as totalPrice from ChoseClothes cc join cc.clothes cl where cc.id = ?1")
    Optional<BigDecimal> findTotalPriceOfChoseClothesById(Long id);

//    @Query("select cl.id from ChoseClothes c join c.clothes cl where c.id=?1")
//    Long findClothesIdByChoseClothesId(Long id);
}
