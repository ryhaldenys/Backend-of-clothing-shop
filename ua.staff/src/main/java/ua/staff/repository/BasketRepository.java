package ua.staff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.staff.dto.BasketDto;
import ua.staff.model.Basket;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket,Long> {

    @Query("select new ua.staff.dto.BasketDto(b.id,b.usedBonuses) " +
           "from Basket b where b.id = ?1")
    Optional<BasketDto> findBasketIdAndUsedBonusesById(Long id);
}
