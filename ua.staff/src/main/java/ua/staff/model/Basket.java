package ua.staff.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "basket", indexes = @Index(name = "basket_person_id_idx",columnList = "person_id"))
public class Basket {
    @Id
    private Long id;

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "person_id")
    @JsonManagedReference
    private Person person;

    private BigDecimal usedBonuses;

    private BigDecimal totalPrice;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "basket",cascade = CascadeType.ALL)
    private List<ChoseClothes> choseClothes = new ArrayList<>();

    public void addClothesToBasket(ChoseClothes foundClothes) {
        choseClothes.add(foundClothes);
        foundClothes.setBasket(this);
    }

    public void removeClothes(){
        choseClothes.forEach(c->c.setBasket(null));
        choseClothes.clear();
    }

    public void clear(){
        this.usedBonuses = BigDecimal.ZERO;
        removeClothes();
    }
}
