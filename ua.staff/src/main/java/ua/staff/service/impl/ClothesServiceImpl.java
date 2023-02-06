package ua.staff.service.impl;

import ua.staff.service.ClothesService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.staff.dto.ClothesDetailsDto;
import ua.staff.dto.FullClothesDto;
import ua.staff.dto.ClothesDto;
import ua.staff.exception.NotFoundException;
import ua.staff.model.Clothes;
import ua.staff.model.ClothesRequestParams;
import ua.staff.model.Image;
import ua.staff.model.Size;
import ua.staff.repository.ClothesRepository;

import java.util.ArrayList;
import java.util.Objects;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClothesServiceImpl implements ClothesService {
    private final ClothesRepository clothesRepository;

    @Override
    public Slice<ClothesDto> getClothesDtos(ClothesRequestParams params, Pageable pageable){
        var clothesSlice = chooseClothes(params,pageable);
        return mapToSliceOfClothesDto(clothesSlice);
    }

    private Slice<Clothes> chooseClothes(ClothesRequestParams params,Pageable pageable){
        Slice<Clothes> clothes;

        if (params.getSubtype()!=null)
            clothes = getClothesFetchImagesAndSizesBySubType(params.getSex(), params.getSubtype(), pageable);
        else if(params.getType()!=null)
            clothes = getClothesFetchImagesAndSizesByType(params.getSex(), params.getType(), pageable);
        else
            clothes = getClothesFetchImagesAndSizes(params.getSex(),pageable);

        return clothes;
    }

    private Slice<Clothes> getClothesFetchImagesAndSizes(String sex, Pageable pageable){
        return clothesRepository.findClothesFetchImagesAndSizesBySex(sex,pageable);
    }

    private Slice<Clothes> getClothesFetchImagesAndSizesBySubType(String sex,String subType, Pageable pageable){
        return clothesRepository.findClothesFetchImagesAndSizesBySexAndSubType(sex,subType,pageable);
    }


    private Slice<Clothes> getClothesFetchImagesAndSizesByType(String sex,String type, Pageable pageable){
        return clothesRepository.findClothesFetchImagesAndSizesBySexAndType(sex,type,pageable);
    }

    private Slice<ClothesDto> mapToSliceOfClothesDto(Slice<Clothes> clothesSlice) {
        checkIsNotEmpty(clothesSlice);
        var clothes = clothesSlice.stream()
                .map(this::mapToClothesDto)
                .toList();

        return new PageImpl<>(clothes);
    }

    private void checkIsNotEmpty(Slice<Clothes> clothes) {
        if (clothes.getContent().isEmpty()){
            throw new NotFoundException("Cannot find any clothes");
        }
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

    @Override
    public FullClothesDto getClothesById(Long id){
        return getFullClothesDto(id);
    }

    private FullClothesDto getFullClothesDto(Long id) {
        var clothesDetails = getClothesDetailsById(id);
        var images = getClothesImagesById(id);
        var sizes = getClothesSizesById(id);

        return getFullClothesDto(clothesDetails,images,sizes);
    }

    private ClothesDetailsDto getClothesDetailsById(Long id){
        return clothesRepository.findClothesById(id)
                .orElseThrow(()-> new NotFoundException("Cannot find clothes by id: "+id));
    }
    private Set<Image> getClothesImagesById(Long id){
        return clothesRepository.findClothesImagesById(id);
    }
    private Set<Size> getClothesSizesById(Long id){
        return clothesRepository.findClothesSizesById(id);
    }
    private FullClothesDto getFullClothesDto(ClothesDetailsDto clothesDetails, Set<Image> images, Set<Size> sizes){
        return new FullClothesDto(clothesDetails,images,sizes);
    }

    @Override
    @Transactional
    public Clothes saveClothes(Clothes clothes){
        return clothesRepository.save(clothes);
    }

    @Override
    @Transactional
    public void deleteClothesById(Long id){
        deleteClothes(id);
    }


    private void deleteClothes(Long id) {
        clothesRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Cannot find and delete clothes by id: "+id));

        clothesRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addImage(Long id,Image image){
        var clothes =clothesRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot add image because clothes by id: "+id+" not found"));
        clothes.addImage(image);
    }

    @Override
    @Transactional
    public void removeImage(Long id,Image image){
        var clothes =clothesRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot remove image because clothes by id: "+id+" not found"));
        clothes.removeImage(image);
    }

    @Override
    @Transactional
    public void addSize(Long id,Size size){
        var clothes =clothesRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot add size because clothes by id: "+id+" not found"));
        clothes.addSize(size);
    }

    @Override
    @Transactional
    public void removeSize(Long id,Size size){
        var clothes =clothesRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot remove size because clothes by id: "+id+" not found"));
        clothes.removeSize(size);
    }
}