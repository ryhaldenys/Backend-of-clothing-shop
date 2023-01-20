package ua.staff.repository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import ua.staff.dto.ClothesDetailsDto;
import ua.staff.generator.ClothesGenerator;
import ua.staff.model.Clothes;
import ua.staff.model.Image;
import ua.staff.model.Size;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class ClothesRepositoryTest {
    @Autowired
    private ClothesRepository repository;

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
    void findClothesSizesByIdTest(){
        Set<Size> setOfSizes = repository.findClothesSizesById(clothesList.get(1).getId());

        assertThat(clothesList.get(0).getSizes().isEmpty()).isTrue();
        assertThat(clothesList.get(1).getSizes()).isEqualTo(setOfSizes);
    }


    @Test
    void findClothesImagesByIdTest(){
        Set<Image> images = repository.findClothesImagesById(clothesList.get(0).getId());

        Set<Image> images2 = repository.findClothesImagesById(clothesList.get(3).getId());

        assertThat(clothesList.get(0).getImages()).isEqualTo(images);
        assertThat(clothesList.get(3).getImages()).isEqualTo(images2);
    }

    @Test
    void findClothesByIdTest(){
        Clothes clothes = clothesList.get(1);
        ClothesDetailsDto clothesDetails = repository.findClothesById(clothes.getId())
                .orElseThrow();

        assertThat(clothesDetails.id()).isEqualTo(clothes.getId());
        assertThat(clothesDetails.name()).isEqualTo(clothes.getName());
        assertThat(clothesDetails.article()).isEqualTo(clothes.getArticle());
    }

    @Test
    void findSizeByClothesIdAndSizeTypeTest(){
        Clothes clothes = clothesList.get(2);
        List<Size> sizes = new ArrayList<>(clothes.getSizes());
        Size size = sizes.get(2);

        Size foundSize = repository.findSizeByClothesIdAndSizeType(clothes.getId(),size.getSize())
                .orElseThrow();

        assertThat(foundSize.getAmount()).isEqualTo(size.getAmount());
        assertThat(foundSize.getSize()).isEqualTo(size.getSize());
    }

    @Test
    void updateAmountOfSizes(){
        Clothes clothes = clothesList.get(1);
        List<Size> sizes = new ArrayList<>(clothes.getSizes());
        Size size = sizes.get(0);

        repository.updateAmountOfSizes(350,clothes.getId(),size.getSize());
        repository.flush();

        Size foundSize = entityManager.getEntityManager().createQuery("select c.sizes from Clothes c join c.sizes s where c.id=?1 and s.size=?2",Size.class)
                .setParameter(1,clothes.getId())
                .setParameter(2,size.getSize())
                .getSingleResult();

        assertThat(foundSize.getAmount()).isEqualTo(350);
    }


    @Test
    void findClothesFetchImagesAndSizesBySexAndType(){
        Clothes clothes = clothesList.get(0);
        Slice<Clothes> clothesSlice = repository
                .findClothesFetchImagesAndSizesBySexAndType("male",clothes.getType(),PageRequest.of(0,clothesList.size()));

        Clothes foundClothes = clothesSlice.getContent().get(0);
        assertThat(clothes.getName()).isEqualTo(foundClothes.getName());
        assertThat(clothes.getArticle()).isEqualTo(foundClothes.getArticle());
    }

    @Test
    void findClothesFetchImagesAndSizesBySexAndSubType(){
        Clothes clothes = clothesList.get(1);
        Slice<Clothes> clothesSlice = repository
                .findClothesFetchImagesAndSizesBySexAndSubType("male",clothes.getSubtype(),PageRequest.of(0,clothesList.size()));

        Clothes foundClothes = clothesSlice.getContent().get(0);

        assertThat(clothes.getName()).isEqualTo(foundClothes.getName());
        assertThat(clothes.getArticle()).isEqualTo(foundClothes.getArticle());
    }



    @Test
    void findClothesJoinFetchImagesAndSizesBySexTest(){

        Slice<Clothes> clothesSlice = repository
                .findClothesFetchImagesAndSizesBySex("male", PageRequest.of(0, clothesList.size()));
        clothesList.sort(Comparator.comparing(Clothes::getAdded_at));

        assertThat(clothesList.get(0).getArticle()).isEqualTo(clothesList.get(0).getArticle());
        assertThat(clothesSlice.getContent()).isEqualTo(clothesList);
    }

}
