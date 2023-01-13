package ua.staff.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ua.staff.model.Order.Status.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(schema = "public",name = "person_order",
        indexes = @Index(name = "order_person_id_idx",columnList = "person_id"))
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false,unique = true)
    private String orderNumber;

    @Column(nullable = false)
    private String personFullName;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = NEW;

    public enum Status{
        NEW,CANCELLED,RECEIVED,
    }

    private BigDecimal usedBonuses;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ChoseClothes> choseClothes = new ArrayList<>();

    @Embedded
    private Delivery deliveryInfo;

    public Order(String orderNumber, BigDecimal totalPrice,String personFullName,
                 Status status, BigDecimal usedBonuses, Person person, List<ChoseClothes> choseClothes,Delivery deliveryInfo) {
        this.orderNumber = orderNumber;
        this.totalPrice = totalPrice;
        this.personFullName = personFullName;
        this.status = status;
        this.usedBonuses = usedBonuses;
        this.person = person;
        this.choseClothes = choseClothes;
        this.deliveryInfo = deliveryInfo;
    }

    public void addBonusesToTotalPrice() {
        totalPrice = totalPrice.subtract(usedBonuses);
    }

    public void addPerson(Person person){
        this.person = person;
        person.addOrder(this);
    }

    public void addChoseClothes(ChoseClothes choseClothes){
        this.choseClothes.add(choseClothes);
        choseClothes.setOrder(this);
    }

    public void addAllChoseClothes(List<ChoseClothes> choseClothes){
        this.choseClothes.addAll(choseClothes);
        choseClothes.forEach(c -> c.setOrder(this));
    }

}
