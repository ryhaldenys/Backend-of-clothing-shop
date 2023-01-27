package ua.staff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.staff.builder.UriBuilder;
import ua.staff.dto.BasketDto;
import ua.staff.model.Size;
import ua.staff.service.BasketService;

import static ua.staff.generator.ResponseEntityGenerator.getResponseEntityWithNoContent;

@RestController
@RequestMapping("/api/people/{person_id}/basket")
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @GetMapping
    @PreAuthorize("hasAuthority('advanced') or authentication.principal.id == #id")
    public BasketDto getBasket(@PathVariable("person_id") Long id){
        return basketService.getBasketElements(id);
    }

    @PostMapping("/{clothes_id}")
    @PreAuthorize("hasAuthority('simple') and authentication.principal.id == #id")
    public ResponseEntity<Void> addToBasket(
            @PathVariable("person_id") Long id,
            @PathVariable("clothes_id")Long clothesId, @RequestBody Size size){

        basketService.addClothesToBasket(id,clothesId,size);
        var location = UriBuilder.createUriFromCurrentServletMapping("/people/{p_id}/basket",id);
        return getResponseEntityWithNoContent(location);
    }

    @DeleteMapping("/{chose_clothes_id}")
    @PreAuthorize("authentication.principal.id == #id")
    public ResponseEntity<Void> removeChoseClothes(@PathVariable("person_id")Long id,
            @PathVariable("chose_clothes_id")Long choseClothesId){

        basketService.removeChoseClothesById(choseClothesId);

        var location = UriBuilder.createUriFromCurrentServletMapping("/people/{p_id}/basket",id);
        return getResponseEntityWithNoContent(location);
    }

    @PatchMapping("/{chose_cl_id}")
    @PreAuthorize("authentication.principal.id == #id")
    public ResponseEntity<Void> updateAmountOfClothes(
            @RequestBody Size size, @PathVariable("person_id") Long id,@PathVariable("chose_cl_id") Long choseClId){

        basketService.updateAmountOfClothes(choseClId,id,size);

        var location = UriBuilder.createUriFromCurrentServletMapping("/people/{p_id}/basket",id);
        return getResponseEntityWithNoContent(location);
    }

    @PatchMapping
    @PreAuthorize("authentication.principal.id == #id")
    public ResponseEntity<Void> addPersonBonuses(@PathVariable("person_id") Long id){
        basketService.addBonuses(id);

        var location = UriBuilder.createUriFromCurrentRequest();
        return getResponseEntityWithNoContent(location);
    }

    @DeleteMapping
    @PreAuthorize("authentication.principal.id == #id")
    public ResponseEntity<Void> removePersonBonuses(@PathVariable("person_id") Long id){

        basketService.removePersonBonuses(id);

        var location = UriBuilder.createUriFromCurrentRequest();
        return getResponseEntityWithNoContent(location);
    }

}
