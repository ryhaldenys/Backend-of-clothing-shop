package ua.staff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.staff.builder.OrderBuilder;
import ua.staff.dto.OrderDto;
import ua.staff.exception.NotFoundException;
import ua.staff.generator.OrderNumberGenerator;
import ua.staff.model.ChoseClothes;
import ua.staff.model.Order;
import ua.staff.model.Person;
import ua.staff.repository.ChoseClothesRepository;
import ua.staff.repository.ClothesRepository;
import ua.staff.repository.OrderRepository;
import ua.staff.repository.PersonRepository;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ChoseClothesRepository choseClothesRepository;
    private final PersonRepository personRepository;
    private final ClothesRepository clothesRepository;


    public void createOrder(Long personId){
        var person = getPersonFetchBasketAndChoseClothesById(personId);
        var order =  createOrder(person);
        saveOrder(order);
        subtractAmountOfOrderedSizes(person);
        clearBasket(person);
    }

    private Person getPersonFetchBasketAndChoseClothesById(Long personId){
        return personRepository.findPersonFetchBasketAndChoseClothesById(personId)
                .orElseThrow(()-> new NotFoundException("Cannot find person by id: "+personId));
    }

    private Order createOrder(Person person){
        var totalPrice = choseClothesRepository.getTotalPriceOfChoseClothesByBasketId(person.getId())
                .orElseThrow();

        var basket  = person.getBasket();

        var order = OrderBuilder.builder()
                .orderNumber(OrderNumberGenerator.generateOrderNumber('M'))
                .totalPrice(totalPrice)
                .usedBonuses(basket.getUsedBonuses())
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

    private void clearBasket(Person person){
        person.getBasket().clear();
    }

    public OrderDto getOrderById(Long orderId) {
        var order = orderRepository.findOrderById(orderId).orElseThrow();
        var choseClothes = choseClothesRepository.findAllByOrderId(orderId);
        order.setChoseClothes(choseClothes);
        return order;
    }
}
