package ua.staff.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.staff.builder.UriBuilder;
import ua.staff.dto.ClothesDto;
import ua.staff.dto.FullClothesDto;
import ua.staff.model.Clothes;

import ua.staff.model.ClothesRequestParams;
import ua.staff.model.Image;
import ua.staff.model.Size;
import ua.staff.service.ClothesService;

import java.net.URI;

import static org.springframework.http.HttpStatus.*;
import static ua.staff.generator.ResponseEntityGenerator.getResponseEntity;
import static ua.staff.generator.ResponseEntityGenerator.getResponseEntityWithNoContent;

@RestController
@RequestMapping("/api/clothes")
@RequiredArgsConstructor
public class ClothesController {
    private final ClothesService clothesService;

    @GetMapping
    public Slice<ClothesDto> getAll(ClothesRequestParams params, Pageable pageable){
        return clothesService.getClothesDtos(params,pageable);
    }

    @GetMapping("/{id}")
    public FullClothesDto getClothesById(@PathVariable("id")Long id){
        return clothesService.getClothesById(id);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('advanced')")
    public ResponseEntity<Clothes> saveClothes(@RequestBody Clothes clothes){
        clothesService.saveClothes(clothes);

        var location = UriBuilder.createUriFromCurrentServletMapping("clothes/{id}",clothes.getId());
        return getResponseEntity(location,CREATED,clothes);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('advanced')")
    public ResponseEntity<Void> deleteClothesById(@PathVariable("id")Long id){
        clothesService.deleteClothesById(id);

        var location = UriBuilder.createUriFromCurrentServletMapping("/clothes");
        return getResponseEntityWithNoContent(location);
    }

    @PostMapping("/{id}/image")
    @PreAuthorize("hasAuthority('advanced')")
    public ResponseEntity<Void> addImage(@PathVariable Long id, @RequestBody Image image){

        clothesService.addImage(id,image);
        var location = buildClothesLocation(id);

        return getResponseEntityWithNoContent(location);
    }

    @DeleteMapping("/{id}/image")
    @PreAuthorize("hasAuthority('advanced')")
    public ResponseEntity<Void> removeImage(@PathVariable Long id, @RequestBody Image image){

        clothesService.removeImage(id,image);
        var location = buildClothesLocation(id);

        return getResponseEntityWithNoContent(location);
    }

    @PostMapping("/{id}/size")
    @PreAuthorize("hasAuthority('advanced')")
    public ResponseEntity<Void> addSize(@PathVariable Long id, @RequestBody Size size){

        clothesService.addSize(id,size);
        var location = buildClothesLocation(id);

        return getResponseEntityWithNoContent(location);
    }

    @DeleteMapping("/{id}/size")
    @PreAuthorize("hasAuthority('advanced')")
    public ResponseEntity<Void> removeSize(@PathVariable Long id, @RequestBody Size size){

        clothesService.removeSize(id,size);
        var location = buildClothesLocation(id);

        return getResponseEntityWithNoContent(location);
    }

    private URI buildClothesLocation(Long id) {
        return UriBuilder.createUriFromCurrentServletMapping("clothes/" + id);
    }

    //todo: add checkout sizes and images isPresent
    //todo: findByArticle
}
