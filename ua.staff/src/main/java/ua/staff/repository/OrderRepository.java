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

    @Query("select new ua.staff.dto.AllOrdersDto(o.id,o.orderNumber,o.totalPrice,o.usedBonuses,o.createdAt,o.status)" +
           " from Order o where o.person.id =?1")
    Slice<AllOrdersDto> findAllByPersonId(Long personId);

    @Query("select new ua.staff.dto.OrderDto(o.id,o.orderNumber,o.totalPrice," +
            "o.status,o.usedBonuses,o.createdAt,o.personFullName,o.deliveryInfo.address," +
            "o.deliveryInfo.deliveryType,o.deliveryInfo.deliveryPrice,o.deliveryInfo.paymentKind) " +
            "from Order o " +
            "where o.id=?1")
    Optional<OrderDto> findOrderById(Long id);

    @Query("select new ua.staff.dto.AllOrdersDto(o.id,o.orderNumber,o.totalPrice,o.usedBonuses,o.createdAt,o.status)" +
            " from Order o")
    Slice<AllOrdersDto> findAllOrders();

    @Query("select o from Order o join fetch o.choseClothes cc join fetch cc.clothes where o.id=?1")
    Optional<Order> findOrderByIdJoinFetchChoseClothesJoinFetchClothes(Long id);

    @Query("select o from Order o left join fetch o.person where o.id=?1")
    Optional<Order> findOrderByIdFetchPerson(Long id);
}
