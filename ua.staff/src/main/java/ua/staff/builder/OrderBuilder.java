package ua.staff.builder;

import ua.staff.model.ChoseClothes;
import ua.staff.model.Delivery;
import ua.staff.model.Order;
import ua.staff.model.Person;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static ua.staff.model.Order.Status.*;

public class OrderBuilder {
    private String orderNumber;
    private BigDecimal totalPrice;
    private String personFullName;
    private Order.Status status = NEW;
    private BigDecimal usedBonuses;
    private Person person;
    private List<ChoseClothes> choseClothes = new ArrayList<>();
    private Delivery delivery;

    private OrderBuilder(){}

    public static OrderBuilder builder(){
        return new OrderBuilder();
    }

    public OrderBuilder orderNumber(String orderNumber){
        this.orderNumber = orderNumber;
        return this;
    }

    public OrderBuilder totalPrice(BigDecimal totalPrice){
        this.totalPrice = totalPrice;
        return this;
    }

    public OrderBuilder personFullName(String personFullName){
        this.personFullName = personFullName;
        return this;
    }

    public OrderBuilder status(Order.Status status){
        this.status = status;
        return this;
    }

    public OrderBuilder usedBonuses(BigDecimal usedBonuses){
        this.usedBonuses = usedBonuses;
        return this;
    }

    public OrderBuilder person(Person person){
        this.person = person;
        return this;
    }

    public OrderBuilder choseClothes(List<ChoseClothes>choseClothes){
        this.choseClothes = choseClothes;
        return this;
    }

    public OrderBuilder delivery(Delivery delivery) {
        this.delivery = delivery;
        return this;
    }

    public Order build(){
        return new Order(orderNumber,totalPrice,personFullName,status,usedBonuses,person,choseClothes,delivery);
    }

}
