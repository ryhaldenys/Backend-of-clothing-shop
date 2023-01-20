package ua.staff.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ua.staff.generator.ClothesGenerator;
import ua.staff.model.Clothes;

import java.util.List;

@DataJpaTest
public class BasketRepositoryTest {
    @Autowired
    private BasketRepository repository;
    @Autowired
    private TestEntityManager entityManager;

    private List<Clothes> clothesList;

    @BeforeEach
    void setUp(){
        clothesList = ClothesGenerator.generateListClothes(10);
        clothesList.get(0).removeSizes();
        clothesList.forEach(entityManager::persist);
        entityManager.flush();
    }

    @Test
    void findBasketIdAndUserBonusesById(){

    }

}
