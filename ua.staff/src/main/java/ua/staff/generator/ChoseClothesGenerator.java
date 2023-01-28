package ua.staff.generator;

import ua.staff.builder.ChoseClothesBuilder;
import ua.staff.model.ChoseClothes;

import static ua.staff.generator.ClothesGenerator.*;

public class ChoseClothesGenerator {

    public static ChoseClothes genereteChoseClothes(int index){
        return ChoseClothesBuilder.builder()
                .sizeKind("Size"+index)
                .amountOfClothes(index)
                .clothes(generateClothes(index))
                .build();
    }

}
