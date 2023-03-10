package ua.staff.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.staff.dto.PeopleDto;
import ua.staff.dto.PersonDto;
import ua.staff.model.Person;
import ua.staff.model.PostAddress;

import java.math.BigDecimal;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person,Long> {
    @Query("select p from Person p join fetch p.basket where p.email =:email")
    Optional<Person> findByEmail(@Param("email") String email);

    @Query("SELECT new ua.staff.dto.PeopleDto(p.id,p.firstName,p.lastName,p.email) from Person p")
    Slice<PeopleDto> findAllPeople(Pageable pageable);

    @Query("SELECT new ua.staff.dto.PersonDto(p.id,p.firstName,p.lastName,p.email,p.bonuses,p.postAddress,p.role) from Person p where p.id = ?1")
    Optional<PersonDto> findPersonDtoById(Long id);

//    @Query("SELECT p.role from Person p where p.id = ?1")
//    Role findPersonRoleById(Long id);

    @Query("select p from Person p left join fetch p.basket where p.id = ?1")
    Optional<Person> findPersonFetchBasketById(Long personId);

    @Query("select distinct p from Person p left join fetch p.basket b left join fetch b.choseClothes where p.id = ?1")
    Optional<Person> findPersonFetchBasketAndChoseClothesById(Long personId);

    @Query("select p.postAddress from Person p where p.id=?1")
    Optional<PostAddress> findPostAddressById(Long id);

    @Query("SELECT p.bonuses from Person p where p.id =?1")
    Optional<BigDecimal> findPersonBonusesById(Long orderId);

    @Modifying
    @Query(value =
            "update person " +
            "set bonuses =:bonuses " +
            "where id =:id",nativeQuery = true)
    void updatePersonBonusesById(@Param("bonuses") BigDecimal bonuses, @Param("id") Long id);
}

