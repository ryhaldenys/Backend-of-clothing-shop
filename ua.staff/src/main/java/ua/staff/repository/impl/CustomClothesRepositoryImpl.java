package ua.staff.repository.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.staff.model.Clothes;
import ua.staff.repository.CustomClothesRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomClothesRepositoryImpl implements CustomClothesRepository {
    private final EntityManager entityManager;

    @Override
    public Slice<Clothes> findClothesFetchImagesAndSizesBySex(String sex, Pageable pageable) {
        var clothes = findAllLeftJoinFetchSizesBySex(sex);
        clothes = findAllFetchImagesByClothes(clothes);
        return mapToPage(pageable,clothes);
    }

    private List<Clothes> findAllLeftJoinFetchSizesBySex(String sex){
        var query = "select c from Clothes c left join fetch c.sizes where c.sex =?1";
        return createQuery(query,sex);
    }

    @Override
    public Slice<Clothes> findClothesFetchImagesAndSizesBySexAndType(String sex, String type, Pageable pageable) {
        var clothes = findAllFetchSizesBySexAndType(sex,type);
        clothes = findAllFetchImagesByClothes(clothes);
        return mapToPage(pageable,clothes);
    }

    private List<Clothes> findAllFetchSizesBySexAndSubType(String sex, String subType){
        var query = "select c from Clothes c left join fetch c.sizes where c.sex =?1 and c.subtype =?2";
        return createQuery(query,sex,subType);
    }


    @Override
    public Slice<Clothes> findClothesFetchImagesAndSizesBySexAndSubType(String sex,String subtype, Pageable pageable) {
        var clothes = findAllFetchSizesBySexAndSubType(sex,subtype);
        clothes = findAllFetchImagesByClothes(clothes);
        return mapToPage(pageable,clothes);
    }

    private List<Clothes> findAllFetchSizesBySexAndType(String sex,String type){
        var query = "select c from Clothes c left join fetch c.sizes where c.sex =?1 and c.type=?2";
        return createQuery(query,sex,type);
    }

    private List<Clothes> findAllFetchImagesByClothes(List<Clothes> clothes){
        var query = "select c from Clothes c left join fetch c.images where c in ?1";
        return createQuery(query,clothes);
    }

    private<P> List<Clothes> createQuery(String query, P param){
        return entityManager.createQuery(query,Clothes.class)
                .setParameter(1,param)
                .getResultList();
    }
    private<P1,P2> List<Clothes> createQuery(String query, P1 param1,P2 param2){
        return entityManager.createQuery(query,Clothes.class)
                .setParameter(1,param1)
                .setParameter(2,param2)
                .getResultList();
    }


    private Slice<Clothes> mapToPage(Pageable pageable,List<Clothes> clothes){
        sortListByAddedTime(clothes);

        int startIndex = getStartIndex(pageable);
        int toIndex = getLastIndex(startIndex,pageable.getPageSize(),clothes.size());

        List<Clothes> newClothesList = getSubList(startIndex,toIndex,clothes);
        return getPage(newClothesList,pageable);
    }

    private void sortListByAddedTime(List<Clothes> newClothesList) {
        newClothesList.sort(Comparator.comparing(Clothes::getAdded_at));
    }

    private int getStartIndex(Pageable pageable) {
        return pageable.getPageNumber() == 0? pageable.getPageNumber() : pageable.getPageNumber()*
                pageable.getPageSize();
    }

    private int getLastIndex(int startIndex, int pageSize, int size) {
        int toIndex = startIndex + pageSize;
        toIndex = Math.min(toIndex, size);
        return toIndex;
    }

    private List<Clothes> getSubList(int startIndex, int toIndex, List<Clothes> clothes) {
        List<Clothes> newClothesList;

        if (startIndex < clothes.size())
            newClothesList = clothes.subList(startIndex, toIndex);
        else
            newClothesList = new ArrayList<>();

        return newClothesList;
    }

    private Slice<Clothes> getPage(List<Clothes> newClothesList, Pageable pageable) {
        return new PageImpl<>(newClothesList,pageable, newClothesList.size());
    }
}
