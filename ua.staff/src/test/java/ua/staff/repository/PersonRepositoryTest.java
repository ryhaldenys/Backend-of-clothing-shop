package ua.staff.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import ua.staff.dto.PeopleDto;
import ua.staff.generator.ClothesGenerator;
import ua.staff.model.Clothes;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TestEntityManager entityManager;

    private List<Clothes> clothes;

    @BeforeEach
    void setUp(){

    }

    @Test
    void findAllPeopleTest(){
        var pageable = Mockito.any(Pageable.class);
        var peopleDtoSlice = personRepository.findAllPeople(pageable);
        var listPeople = peopleDtoSlice.getContent();

        //assertThat(listPeople.get(0).firstName()).isEqualTo()
    }

}
