package ua.staff.builder;

import ua.staff.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;

public class PersonBuilder {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private final LocalDateTime createdAt = LocalDateTime.now();
    private Role role = Role.USER;
    private String status ="ACTIVE";
    private PostAddress postAddress;
    private BigDecimal bonuses = ZERO;
    private Basket basket;
    private List<Order> orders = new ArrayList<>();

    private PersonBuilder(){}

    public static PersonBuilder builder(){
        return new PersonBuilder();
    }

    public PersonBuilder id(Long id){
        this.id = id;
        return this;
    }

    public PersonBuilder firstName(String firstName){
        this.firstName = firstName;
        return this;
    }

    public PersonBuilder lastName(String lastName){
        this.lastName = lastName;
        return this;
    }

    public PersonBuilder password(String password){
        this.password = password;
        return this;
    }

    public PersonBuilder email(String email){
        this.email = email;
        return this;
    }

    public PersonBuilder role(Role role){
        this.role = role;
        return this;
    }

    public PersonBuilder postAddress(PostAddress postAddress){
        this.postAddress = postAddress;
        return this;
    }

    public PersonBuilder bonuses(BigDecimal bonuses){
        this.bonuses = bonuses;
        return this;
    }

    public PersonBuilder basket(Basket basket){
        this.basket = basket;
        return this;
    }

    public PersonBuilder status(String status){
        this.status = status;
        return this;
    }

    public PersonBuilder orders(List<Order> orders){
        this.orders = orders;
        return this;
    }

    public Person build(){
        return new Person(id,firstName,lastName,password,
                email, createdAt,role,status,postAddress,bonuses,basket,orders);
    }
}
