package ua.staff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.staff.builder.OrderBuilder;
import ua.staff.dto.OrderDto;
import ua.staff.exception.NotFoundException;
import ua.staff.generator.OrderNumberGenerator;
import ua.staff.model.*;
import ua.staff.repository.ChoseClothesRepository;
import ua.staff.repository.ClothesRepository;
import ua.staff.repository.OrderRepository;
import ua.staff.repository.PersonRepository;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.*;
import static ua.staff.model.Order.Status.*;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ChoseClothesRepository choseClothesRepository;
    private final PersonRepository personRepository;
    private final ClothesRepository clothesRepository;

    private final BigDecimal BONUS_PERCENTAGE = valueOf(0.03);


    public void createOrder(Long personId, Delivery delivery){
        var person = getPersonFetchBasketAndChoseClothesById(personId);
        var order =  createOrder(person,delivery);
        saveOrder(order);
        subtractAmountOfOrderedSizes(person);
        usePersonBonuses(person);
        clearBasket(person);
    }

    private Person getPersonFetchBasketAndChoseClothesById(Long personId){
        return personRepository.findPersonFetchBasketAndChoseClothesById(personId)
                .orElseThrow(()-> new NotFoundException("Cannot find person by id: "+personId));
    }

    private Order createOrder(Person person,Delivery delivery){
        var totalPrice = choseClothesRepository.findTotalPriceOfChoseClothesByBasketId(person.getId())
                .orElseThrow(()->new NotFoundException("Cannot find total price of chose clothes by basket id: "+person.getId()));
        var basket  = person.getBasket();

        var order = OrderBuilder.builder()
                .orderNumber(OrderNumberGenerator.generateOrderNumber('M'))
                .totalPrice(totalPrice)
                .personFullName(person.getFirstName()+" "+person.getLastName())
                .usedBonuses(basket.getUsedBonuses())
                .delivery(delivery)
                .build();

        order.addPerson(person);
        order.addAllChoseClothes(person.getBasket().getChoseClothes());
        order.addBonusesToTotalPrice();

        return order;
    }

    private void saveOrder(Order order){
        orderRepository.save(order);
    }

    private void subtractAmountOfOrderedSizes(Person person) {
        var choseClothes = person.getBasket().getChoseClothes();
        choseClothes.forEach(this::processAmount);
    }

    private void processAmount(ChoseClothes choseClothes) {
        var sizeKind = choseClothes.getSizeKind();
        var amount = choseClothes.getAmountOfClothes();
        var clothesId = choseClothes.getClothes().getId();

        var size = clothesRepository.findSizeByClothesIdAndSizeType(clothesId,sizeKind).orElseThrow();
        clothesRepository.updateAmountOfSizes(size.getAmount()-amount,clothesId,sizeKind);
    }

    private void usePersonBonuses(Person person) {
        var basket = person.getBasket();
        person.setBonuses(person.getBonuses().subtract(basket.getUsedBonuses()));
    }

    private void clearBasket(Person person){
        person.getBasket().clear();
    }



    public OrderDto getOrderById(Long orderId) {
        var order = orderRepository.findOrderById(orderId).orElseThrow();
        var choseClothes = choseClothesRepository.findAllByOrderId(orderId);
        order.setChoseClothes(choseClothes);
        return order;
    }


    public void setStatusCanceledByOrderId(Long orderId) {
        var order = getOrderFetchChoseClothesFetchClothes(orderId);
        var personBonuses = getPersonBonuses(orderId);
        updatePersonBonuses(personBonuses,order);

        returnClothes(order);
        setStatusCanceled(order);
    }

    private Order getOrderFetchChoseClothesFetchClothes(Long orderId){
        return orderRepository.findOrderByIdJoinFetchChoseClothesJoinFetchClothes(orderId)
                .orElseThrow(()->new NotFoundException("Cannot find an order by id: "+orderId));

    }

    private BigDecimal getPersonBonuses(Long orderId){
        return personRepository.findPersonBonusesByOrderId(orderId)
                .orElseThrow(()->new NotFoundException("Cannot find person's bonuses by orderId: "+orderId));
    }

    private void updatePersonBonuses(BigDecimal personBonuses, Order order){
        var addedBonuses = personBonuses.add(order.getUsedBonuses());
        personRepository.updatePersonBonusesByOrderId(addedBonuses,order.getId());
    }


    private void returnClothes(Order order) {
        order.getChoseClothes().forEach(this::updateAmountOfClothes);
    }

    private void updateAmountOfClothes(ChoseClothes choseClothes) {
        var sizeKind = choseClothes.getSizeKind();
        var amountOfClothes = choseClothes.getAmountOfClothes();
        var clothesId = choseClothes.getClothes().getId();
        var size = clothesRepository.findSizeByClothesIdAndSizeType(clothesId,sizeKind).orElseThrow();
        clothesRepository.updateAmountOfSizes(size.getAmount()+amountOfClothes,clothesId,sizeKind);
    }

    private void setStatusCanceled(Order order){
        if (order.getStatus().equals(NEW))
            order.setStatus(CANCELLED);
        else
            throw new IllegalStateException("Cannot chane status "+order.getStatus()+"to "+CANCELLED);
    }


    public void setStatusReceivedByOrderId(Long id) {
        var order = orderRepository.findOrderByIdFetchPerson(id).orElseThrow();
        var person = order.getPerson();
        if (order.getStatus().equals(NEW))
            order.setStatus(RECEIVED);
        else
            throw new IllegalStateException("Cannot chane status "+order.getStatus()+"to "+RECEIVED);

        var totalPrice = order.getTotalPrice().add(order.getUsedBonuses());
        var bonuses = totalPrice.multiply(BONUS_PERCENTAGE);
        bonuses = bonuses.setScale(0, HALF_UP);

        person.setBonuses(person.getBonuses().add(bonuses));
    }
}
