package ua.staff.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ua.staff.builder.ChoseClothesBuilder;
import ua.staff.builder.PersonBuilder;
import ua.staff.dto.BasketDto;
import ua.staff.generator.ClothesGenerator;
import ua.staff.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class BasketRepositoryTest {
    @Autowired
    private BasketRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    private Person person;

    @BeforeEach
    void setUp(){
        List<Clothes> clothesList = ClothesGenerator.generateListClothes(2);
        clothesList.forEach(entityManager::persist);

        ChoseClothes choseClothes = createChoseClothes(clothesList.get(1));
        person = createPersonWithBasket(choseClothes);

        entityManager.persist(person);
        entityManager.flush();
    }

    @Test
    void findBasketIdAndUserBonusesById(){
        BasketDto basketDto = repository.findBasketIdAndUsedBonusesById(person.getId())
                .orElseThrow();

        assertThat(basketDto.getUsedBonuses()).isEqualByComparingTo(person.getBonuses());
        assertThat(basketDto.getId()).isEqualByComparingTo(person.getId());
    }


    private ChoseClothes createChoseClothes(Clothes clothes) {
        ChoseClothes choseClothes = ChoseClothesBuilder.builder()
                .amountOfClothes(1)
                .sizeKind("XS")
                .build();
        choseClothes.addClothes(clothes);
        return choseClothes;
    }

    private Person createPersonWithBasket(ChoseClothes choseClothes) {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role("USER"));

        Person person = PersonBuilder.builder()
                .firstName("firstName")
                .lastName("lastName")
                .numberPhone("+38098098765")
                .password("qwerty")
                .roles(roles)
                .bonuses(BigDecimal.valueOf(100))
                .build();

        Basket basket = new Basket();
        person.addBasket(basket);

        basket.addClothesToBasket(choseClothes);
        basket.setUsedBonuses(person.getBonuses());

        return person;
    }
}
