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
            images.add(new Image(true,"/images/image"+i));

            Clothes clothes = ClothesBuilder.builder().name("name"+i).article("article"+i).description("description"+i)
                    .sex("male").type("type"+i).subType("subType"+i)
                    .price(BigDecimal.valueOf(1200+i)).discount(BigDecimal.valueOf(0.15)).images(images)
                    .sizes(getSizes())
                    .build();

            clothesList.add(clothes);
        }
        return clothesList;
    }


    public static Clothes generateClothes(int value){
        return ClothesBuilder.builder().name("name"+value).article("article"+value).description("description"+value)
                .sex("male").type("type"+value).subType("subType"+value)
                .discount(BigDecimal.valueOf(0.015))
                .price(BigDecimal.valueOf(1200+value)).images(Set.of(new Image(true,"/images/image"+value)))
                .sizes(getSizes())
                .build();
    }


    private static Set<Size> getSizes(){
        Set<Size>sizes = new HashSet<>();
        sizes.add(new Size("XS",50));
        sizes.add(new Size("S",100));
        sizes.add(new Size("M",150));
        sizes.add(new Size("L",200));
        return sizes;
    }

}
