package ua.staff.service;

import ua.staff.dto.BasketDto;
import ua.staff.model.Size;

public interface BasketService {
    BasketDto getBasketElements(Long personId);
    void addClothesToBasket(Long personId, Long clothesId, Size size);
    void updateAmountOfClothes(Long choseClothesId,Long personId,Size size);
    void addBonuses(Long personId);
    void removePersonBonuses(Long personId);
    void removeChoseClothesById(Long choseClothesId);
}
