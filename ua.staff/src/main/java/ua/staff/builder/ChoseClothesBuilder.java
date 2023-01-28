package ua.staff.builder;

import ua.staff.model.Basket;
import ua.staff.model.ChoseClothes;
import ua.staff.model.Clothes;
import ua.staff.model.Order;

public class ChoseClothesBuilder {
    private Long id;
    private Integer amountOfClothes = 1;
    private String sizeKind;
    private Clothes clothes;
    private Basket basket;
    private Order order;

    private ChoseClothesBuilder(){}

    public static ChoseClothesBuilder builder(){
        return new ChoseClothesBuilder();
    }

    public ChoseClothesBuilder id(Long id){
        this.id = id;
        return this;
    }

    public ChoseClothesBuilder amountOfClothes(Integer amountOfClothes){
        this.amountOfClothes = amountOfClothes;
        return this;
    }

    public ChoseClothesBuilder sizeKind(String sizeKind){
        this.sizeKind = sizeKind;
        return this;
    }

    public ChoseClothesBuilder clothes(Clothes clothes){
        this.clothes = clothes;
        return this;
    }

    public ChoseClothesBuilder basket(Basket basket){
        this.basket= basket;
        return this;
    }

    public ChoseClothesBuilder order(Order order){
        this.order = order;
        return this;
    }

    public ChoseClothes build(){
        return new ChoseClothes(id,amountOfClothes,sizeKind,clothes,basket,order);
    }
}
