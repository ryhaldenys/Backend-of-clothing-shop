package ua.staff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.staff.dto.ClothesDetailsDto;
import ua.staff.dto.FullClothesDto;
import ua.staff.dto.ClothesDto;
import ua.staff.dto.PersonRolesDto;
import ua.staff.exception.NotFoundException;
import ua.staff.model.Clothes;
import ua.staff.model.Image;
import ua.staff.model.Size;
import ua.staff.repository.ClothesRepository;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClothesService {


    private final ClothesRepository clothesRepository;


    public Slice<ClothesDto> getClothesDto(String sex,Pageable pageable){

        var clothesSlice = clothesRepository.findClothesJoinFetchImagesAndSizes(sex,pageable);

        return mapToSliceOfClothesDto(clothesSlice);
    }

    private Slice<ClothesDto> mapToSliceOfClothesDto(Slice<Clothes> clothesSlice) {
        var clothes = clothesSlice.stream()
                .map(this::mapToClothesDto)
                .toList();

        checkIsNotEmpty(clothes);
        return new PageImpl<>(clothes);
    }

    private ClothesDto mapToClothesDto(Clothes clothes){
        return new ClothesDto(clothes.getId(),clothes.getName(),clothes.getPrice(), getMainImageUrl(clothes), getJoinedSizes(clothes));
    }

    private String getMainImageUrl(Clothes clothes) {
        var listImages = new ArrayList<>(clothes.getImages());
        var defaultUrl = listImages.get(0).getUrl();

        return listImages.stream().filter(c->!c.getIsMain().equals(false))
                .map(Image::getUrl)
                .findFirst().orElse(defaultUrl);
    }

    private String getJoinedSizes(Clothes clothes) {

        return clothes.getSizes().stream()
                .map(Size::getSize)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));

    }

    private void checkIsNotEmpty(List<ClothesDto> clothes) {
        if (clothes.isEmpty()){
            throw new NotFoundException("Cannot find any clothes");
        }
    }



    public FullClothesDto getClothesById(Long id){
        return createFullClothesDto(id);
    }

    private FullClothesDto createFullClothesDto(Long id) {
        var images = clothesRepository.findClothesImagesById(id);
        var sizes = clothesRepository.findClothesSizesById(id);

        ClothesDetailsDto clothesDetails = clothesRepository.findClothesById(id)
                .orElseThrow(()-> new NotFoundException("Cannot find clothes by id: "+id));

        return new FullClothesDto(clothesDetails,images,sizes);
    }


    @Transactional
    public Clothes saveClothes(Clothes clothes){
        return clothesRepository.save(clothes);
    }

    @Transactional
    public void deleteClothesById(Long id){
        deleteClothes(id);
    }


    private void deleteClothes(Long id) {
        clothesRepository.findClothesArticleById(id)
                .orElseThrow(()-> new NotFoundException("Cannot find and delete clothes by id: "+id));

        clothesRepository.deleteById(id);
    }

    @Transactional
    public void addImage(Long id,Image image){
        var clothes =clothesRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot add image because clothes by id: "+id+" not found"));
        clothes.addImage(image);
    }

    @Transactional
    public void removeImage(Long id,Image image){
        var clothes =clothesRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot remove image because clothes by id: "+id+" not found"));

        clothes.removeImage(image);
    }

    @Transactional
    public void addSize(Long id,Size size){
        var clothes =clothesRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot add size because clothes by id: "+id+" not found"));

        clothes.addSize(size);
    }


    public void removeSize(Long id,Size size){
        var clothes =clothesRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot remove size because clothes by id: "+id+" not found"));

        clothes.removeSize(size);
    }
}
