package ua.staff.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chose_clothes",
        indexes ={@Index(name = "chose_clothes_basket_id",columnList = "basket_id")
                ,@Index(name = "chose_clothes_order_id",columnList = "order_id")}
)
public class ChoseClothes {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("1")
    private Integer amountOfClothes = 1;

    @Column(nullable = false)
    private String sizeKind;


    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    @JoinColumn(name = "clothes_id")
    private Clothes clothes;

    public void addClothes(Clothes clothes){
        this.clothes = clothes;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public ChoseClothes(String sizeKind) {
        this.sizeKind = sizeKind;
        this.amountOfClothes = 1;
    }
}
