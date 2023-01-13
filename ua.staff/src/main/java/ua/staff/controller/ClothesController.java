package ua.staff.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.staff.builder.UriBuilder;
import ua.staff.dto.ClothesDto;
import ua.staff.dto.FullClothesDto;
import ua.staff.model.Clothes;

import ua.staff.model.Image;
import ua.staff.model.Size;
import ua.staff.service.ClothesService;

import java.net.URI;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/clothes")
@RequiredArgsConstructor
public class ClothesController {
    private final ClothesService clothesService;

    @GetMapping
    public Slice<ClothesDto> getAll(@RequestParam(value = "sex",defaultValue = "male")String sex,Pageable pageable){
        return clothesService.getClothesDto(sex,pageable);
    }

    @GetMapping("/{id}")
    public FullClothesDto getClothesById(@PathVariable("id")Long id){
        return clothesService.getClothesById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<Clothes> saveClothes(@RequestBody Clothes clothes){
        clothesService.saveClothes(clothes);

        var location = UriBuilder.createUriFromCurrentServletMapping("clothes/{id}",clothes.getId());
        return ResponseEntity.created(location)
                .body(clothes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Clothes> deleteClothesById(@PathVariable("id")Long id){

        clothesService.deleteClothesById(id);

        var location = UriBuilder.createUriFromCurrentServletMapping("/clothes");

        return ResponseEntity.status(204)
                .location(location)
                .build();
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Clothes> addImage(@PathVariable Long id, @RequestBody Image image){

        clothesService.addImage(id,image);
        var location = buildClothesLocation(id);

        return createResponseEntity(NO_CONTENT,location);
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Clothes> removeImage(@PathVariable Long id, @RequestBody Image image){

        clothesService.removeImage(id,image);
        var location = buildClothesLocation(id);

        return createResponseEntity(NO_CONTENT,location);
    }

    @PostMapping("/{id}/size")
    public ResponseEntity<Clothes> addSize(@PathVariable Long id, @RequestBody Size size){

        clothesService.addSize(id,size);
        var location = buildClothesLocation(id);

        return createResponseEntity(NO_CONTENT,location);
    }

    @DeleteMapping("/{id}/size")
    public ResponseEntity<Clothes> removeSize(@PathVariable Long id, @RequestBody Size size){

        clothesService.removeSize(id,size);
        var location = buildClothesLocation(id);

        return createResponseEntity(NO_CONTENT,location);
    }



    private URI buildClothesLocation(Long id) {
        return UriBuilder.createUriFromCurrentServletMapping("clothes/" + id);
    }

    private ResponseEntity<Clothes> createResponseEntity(HttpStatus status, URI location) {
        return ResponseEntity.status(status).location(location).build();
    }



}
