package ua.staff.repository;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.staff.dto.AllOrdersDto;
import ua.staff.dto.OrderDto;
import ua.staff.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("select new ua.staff.dto.AllOrdersDto(o.id,o.orderNumber,o.totalPrice,o.usedBonuses,o.created_at,o.status)" +
           " from Order o where o.person.id =?1")
    Slice<AllOrdersDto> findAllByPersonId(Long personId);

    @Query("select new ua.staff.dto.OrderDto(o.id,o.orderNumber,o.totalPrice,o.status,o.usedBonuses,o.created_at) from Order o where o.id=?1")
    Optional<OrderDto> findOrderById(Long id);
}