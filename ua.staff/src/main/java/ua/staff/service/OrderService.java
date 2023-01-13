package ua.staff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.staff.builder.OrderBuilder;
import ua.staff.dto.OrderDto;
import ua.staff.exception.NotFoundException;
import ua.staff.generator.OrderNumberGenerator;
import ua.staff.model.ChoseClothes;
import ua.staff.model.Delivery;
import ua.staff.model.Order;
import ua.staff.model.Person;
import ua.staff.repository.ChoseClothesRepository;
import ua.staff.repository.ClothesRepository;
import ua.staff.repository.OrderRepository;
import ua.staff.repository.PersonRepository;

import java.math.BigDecimal;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ChoseClothesRepository choseClothesRepository;
    private final PersonRepository personRepository;
    private final ClothesRepository clothesRepository;


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
        var totalPrice = choseClothesRepository.getTotalPriceOfChoseClothesByBasketId(person.getId())
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
        var size = clothesRepository.findSizeByClothesIdAndSizeType(choseClothes.getClothes().getId(),sizeKind).orElseThrow();
        clothesRepository.updateAmountOfSizes(size.getAmount()+amountOfClothes,choseClothes.getClothes().getId(),sizeKind);
    }

    private void setStatusCanceled(Order order){
        order.setStatus(Order.Status.CANCELLED);
    }
}
