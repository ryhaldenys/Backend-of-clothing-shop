package ua.staff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.staff.builder.OrderBuilder;
import ua.staff.dto.AllOrdersDto;
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


    @Cacheable(value = "person_orders", key = "#personId")
    public Slice<AllOrdersDto> getAllOrders(Long personId){
        System.out.println("cache was not used in order service");
        return orderRepository.findAllByPersonId(personId);
    }

    @Caching(evict = {
            @CacheEvict(value = "person_orders",key = "#personId"),
            @CacheEvict(value = "all_orders",allEntries = true)})
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

    @Caching(evict = {
            @CacheEvict(value = "person_orders",key = "#personId"),
            @CacheEvict(value = "all_orders",allEntries = true)})
    public void setStatusCanceledByOrderId(Long personId, Long orderId) {
        var order = getOrderFetchChoseClothesFetchClothes(personId,orderId);
        var personBonuses = getPersonBonuses(personId);
        var addedUsedBonuses = addUsedBonusesToUser(personBonuses,order);
        updatePersonBonuses(addedUsedBonuses,personId);
        returnClothes(order);
        setStatusCanceled(order);
    }

    private Order getOrderFetchChoseClothesFetchClothes(Long personId,Long orderId){
        return orderRepository.findOrderByIdJoinFetchChoseClothesJoinFetchClothes(personId,orderId)
                .orElseThrow(()->new NotFoundException("Cannot " +
                        "find an order by person id: "+personId+" and order id: "+orderId));

    }

    private BigDecimal getPersonBonuses(Long personId){
        return personRepository.findPersonBonusesById(personId)
                .orElseThrow(()->new NotFoundException("Cannot find person's bonuses by personId: "+personId));
    }

    private BigDecimal addUsedBonusesToUser(BigDecimal personBonuses,Order order) {
        return personBonuses.add(order.getUsedBonuses());

    }

    private void updatePersonBonuses(BigDecimal addedBonuses, Long personId){
        personRepository.updatePersonBonusesByOrderId(addedBonuses,personId);
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
            throw new IllegalStateException("Cannot chane status "+order.getStatus()+" to "+CANCELLED);
    }

    @Caching(evict = {
            @CacheEvict(value = "person_orders",key = "#personId"),
            @CacheEvict(value = "all_orders",allEntries = true)})
    public void setStatusReceivedByOrderId(Long personId, Long orderId) {
        var order = getOrderFetchPerson(personId,orderId);
        setStatusReceived(order);
        addBonusesToUser(order);
    }

    private Order getOrderFetchPerson(Long personId, Long orderId){
        return orderRepository.findOrderByIdFetchPerson(personId,orderId)
                .orElseThrow(()->new NotFoundException("Cannot find " +
                        "order by personId: "+personId+" and order id: "+orderId));
    }

    private void setStatusReceived(Order order) {
        if (order.getStatus().equals(NEW))
            order.setStatus(RECEIVED);
        else
            throw new IllegalStateException("Cannot chane status "+order.getStatus()+" to "+RECEIVED);
    }

    private void addBonusesToUser(Order order) {
        var person = order.getPerson();
        var totalPrice = order.getTotalPrice().add(order.getUsedBonuses());
        var bonuses = totalPrice.multiply(BONUS_PERCENTAGE);
        bonuses = bonuses.setScale(0, HALF_UP);

        person.setBonuses(person.getBonuses().add(bonuses));
    }
}
