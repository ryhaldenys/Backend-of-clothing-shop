package ua.staff.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
public class CustomClothesRepositoryImpl implements CustomClothesRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Slice<Clothes> findClothesJoinFetchImagesAndSizes(String sex,Pageable pageable) {
        List<Clothes> clothes = findAllLeftJoinFetchSizes(sex);
        clothes = findAllLeftJoinFetchImages(clothes);
        return mapToPage(pageable,clothes);
    }

    private List<Clothes> findAllLeftJoinFetchSizes(String sex){
        return entityManager
                .createQuery("select c from Clothes c left join fetch c.sizes where c.sex =?1",Clothes.class)
                .setParameter(1,sex)
                .getResultList();
    }

    private List<Clothes> findAllLeftJoinFetchImages(List<Clothes> clothes){
        return entityManager.createQuery("select c from Clothes c left join fetch c.images where c in ?1",Clothes.class)
                .setParameter(1,clothes)
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
