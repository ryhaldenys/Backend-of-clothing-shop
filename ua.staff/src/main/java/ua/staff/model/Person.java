package ua.staff.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "numberPhone")
@AllArgsConstructor
@Entity
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String numberPhone;

    @Column(nullable = false)
    private LocalDateTime created_at = LocalDateTime.now();

    @ElementCollection
    @CollectionTable(name = "person_roles",
            joinColumns = @JoinColumn(name = "person_id", nullable = false),
            indexes = @Index(name = "person_roles_person_id_idx", columnList = "person_id"))
    private List<Role> roles = new ArrayList<>();

    @Embedded
    private PostAddress postAddress;

    @ColumnDefault("0")
    @Column(nullable = false)
    private BigDecimal bonuses = ZERO;

    @OneToOne(mappedBy = "person", orphanRemoval = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private Basket basket;

    public void addBasket(Basket basket){
        basket.setPerson(this);
        this.setBasket(basket);
    }

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();


    public void addOrder(Order order) {
        orders.add(order);
    }

}
