package ua.staff.generator;

import ua.staff.builder.ClothesBuilder;
import ua.staff.model.Clothes;
import ua.staff.model.Image;
import ua.staff.model.Size;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ClothesGenerator {

    public static List<Clothes> generateListClothes(int count){
        List<Clothes>clothesList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Set<Image>images = new HashSet<>();
            images.add(new Image(true,"/first/url"+i));

            Clothes clothes = ClothesBuilder.builder().name("name"+i).article("dd32w"+i).description("clothes"+i)
                    .sex("male").type("choose"+i).subType("trainers"+i)
                    .price(BigDecimal.valueOf(1200+i)).discount(BigDecimal.valueOf(0.01+i)).images(images)
                    .sizes(getSizes())
                    .build();

            clothesList.add(clothes);
        }
        return clothesList;
    }


    public static Clothes generateClothes(int value){
        return ClothesBuilder.builder().name("name"+value).article("2232w"+value).description("clothes"+value)
                .sex("male").type("choose"+value).subType("trainers"+value)
                .discount(BigDecimal.valueOf(0.12))
                .price(BigDecimal.valueOf(1200)).images(Set.of(new Image(true,"/first/url"+value)))
                .sizes(getSizes())
                .build();
    }


    private static Set<Size> getSizes(){
        Set<Size>sizes = new HashSet<>();
        sizes.add(new Size("XS",100));
        sizes.add(new Size("S",144));
        sizes.add(new Size("M",110));
        sizes.add(new Size("L",10));
        return sizes;
    }

}
