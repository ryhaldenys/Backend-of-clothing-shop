package ua.staff.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import ua.staff.generator.ClothesGenerator;
import ua.staff.model.Clothes;
import ua.staff.model.Image;
import ua.staff.model.Size;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class ClothesRepositoryTest {
    @Autowired
    private ClothesRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    private List<Clothes> clothes;

    @BeforeEach
    void setUp(){
        clothes = ClothesGenerator.generateListClothes(10);
        clothes.get(0).removeSizes();
        clothes.forEach(entityManager::persist);
        entityManager.flush();
    }


    @Test
    void findClothesSizesByIdTest(){
        Set<Size> setOfSizes = repository.findClothesSizesById(clothes.get(1).getId());

        assertThat(clothes.get(0).getSizes().isEmpty()).isTrue();
        assertThat(clothes.get(1).getSizes()).isEqualTo(setOfSizes);
    }


    @Test
    void findClothesImagesByIdTest(){
        Set<Image> images = repository.findClothesImagesById(clothes.get(0).getId());

        Set<Image> images2 = repository.findClothesImagesById(clothes.get(3).getId());

        assertThat(clothes.get(0).getImages()).isEqualTo(images);
        assertThat(clothes.get(3).getImages()).isEqualTo(images2);

    }

    @Test
    void findClothesJoinFetchImagesAndSizesTest(){

        Slice<Clothes> clothesSlice = repository
                .findClothesJoinFetchImagesAndSizes("male", PageRequest.of(0,clothes.size()));
        clothes.sort(Comparator.comparing(Clothes::getAdded_at));

        assertThat(clothes.get(0).getArticle()).isEqualTo(clothes.get(0).getArticle());
        assertThat(clothesSlice.getContent()).isEqualTo(clothes);
    }

}
