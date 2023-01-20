package ua.staff.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;
import ua.staff.dto.ClothesDto;
import ua.staff.dto.FullClothesDto;
import ua.staff.generator.ClothesGenerator;
import ua.staff.model.Clothes;
import ua.staff.model.ClothesRequestParams;
import ua.staff.model.Image;
import ua.staff.model.Size;
import ua.staff.repository.ClothesRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class ClothesServiceTest {
    @Autowired
    private ClothesService clothesService;

    @Autowired
    private ClothesRepository clothesRepository;

    private List<Clothes> clothes;

    @BeforeEach
    void setUp(){
        clothes = ClothesGenerator.generateListClothes(5);
        clothesRepository.saveAll(clothes);
    }

    @Test
    void getClothesDtoTest(){
        Slice<ClothesDto> clothesDto = clothesService.getClothesDtos(new ClothesRequestParams(), PageRequest.of(0,clothes.size()));

        assertThat(clothesDto.getContent().get(0).id()).isEqualTo(clothes.get(0).getId());
        assertThat(clothesDto.getContent().get(1).id()).isEqualTo(clothes.get(1).getId());
        assertThat(clothesDto.getContent().get(2).name()).isEqualTo(clothes.get(2).getName());
    }

    @Test
    void getClothesById(){
        var element = clothes.get(1);

        FullClothesDto clothesDto = clothesService.getClothesById(element.getId());
        assertThat(clothesDto.clothesDetails().article()).isEqualTo(element.getArticle());
        assertThat(clothesDto.clothesDetails().name()).isEqualTo(element.getName());
    }

    @Test
    void saveClothesTest(){
        var newClothes = ClothesGenerator.generateClothes(1232);
        clothesService.saveClothes(newClothes);
        var foundClothes  = clothesRepository.findById(newClothes.getId()).orElseThrow();
        assertThat(foundClothes.getArticle()).isEqualTo(newClothes.getArticle());
    }


    @Test
    void deleteClothesTest(){
        clothesService.deleteClothesById(clothes.get(0).getId());

        assertThatThrownBy(
                ()->clothesRepository.findClothesById(clothes.get(0).getId()).orElseThrow()
        );

    }


    @Test
    @Transactional
    void addSize() {
        var size = new Size("new size",40);
        clothesService.addSize(clothes.get(0).getId(), size);

        Set<Size> sizes = clothesRepository.findClothesSizesById(clothes.get(0).getId());

        assertThat(clothes.get(0).getSizes()).isEqualTo(sizes);
    }

    @Test
    @Transactional
    void removeSize() {
        var foundClothes = clothes.get(0);
        var size = new ArrayList<>(foundClothes.getSizes()).get(0);
        clothesService.removeSize(foundClothes.getId(),size);

        Set<Size> sizes = clothesRepository.findClothesSizesById(clothes.get(0).getId());

        assertThat(sizes.contains(size)).isFalse();
    }

    @Test
    @Transactional
    void addImage() {
        var foundClothes = clothes.get(0);
        var image = new Image(false,"new image");
        clothesService.addImage(foundClothes.getId(),image);

        Set<Image> images = clothesRepository.findClothesImagesById(foundClothes.getId());

        assertThat(foundClothes.getImages()).isEqualTo(images);
    }

    @Test
    @Transactional
    void removeImage() {
        var foundClothes = clothes.get(0);
        var image = new ArrayList<>(foundClothes.getImages()).get(0);
        clothesService.removeImage(foundClothes.getId(),image);

        Set<Image> images = clothesRepository.findClothesImagesById(clothes.get(0).getId());

        assertThat(images.contains(image)).isFalse();
    }


    @AfterEach
    void afterEach(){
        clothesRepository.deleteAll();
    }


}

