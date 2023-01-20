package ua.staff.builder;


import ua.staff.model.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ClothesBuilder{
    private Long id;
    private String name;
    private String article;
    private String type;
    private String subType;
    private String description;
    private final LocalDateTime added_at = LocalDateTime.now();
    private BigDecimal price;
    private BigDecimal discount;
    private String sex;
    private Set<Image> images = new HashSet<>();
    private Set<Size> sizes =  new HashSet<>();


    private ClothesBuilder(){}

    public static ClothesBuilder builder(){
        return new ClothesBuilder();
    }

    public ClothesBuilder id(Long id){
        this.id = id;
        return this;
    }

    public ClothesBuilder name(String name){
        this.name = name;
        return this;
    }


    public ClothesBuilder article(String article){
        this.article = article;
        return this;
    }

    public ClothesBuilder type(String type){
        this.type = type;
        return this;
    }

    public ClothesBuilder subType(String subType){
        this.subType = subType;
        return this;
    }

    public ClothesBuilder description(String description){
        this.description = description;
        return this;
    }

    public ClothesBuilder price(BigDecimal price){
        this.price = price;
        return this;
    }

    public ClothesBuilder sex(String sex){
        this.sex = sex;
        return this;
    }

    public ClothesBuilder discount(BigDecimal discount){
        this.discount = discount;
        return this;
    }

    public ClothesBuilder images(Set<Image> images){
        this.images = images;
        return this;
    }

    public ClothesBuilder sizes(Set<Size> sizes){
        this.sizes = sizes;
        return this;
    }

    public Clothes build(){
        return new Clothes(id,name,article,type,subType,
                description,added_at,price,discount,sex,images,sizes);
    }
}
