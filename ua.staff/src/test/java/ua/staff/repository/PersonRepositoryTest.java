package ua.staff.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

import ua.staff.generator.ChoseClothesGenerator;
import ua.staff.generator.PersonGenerator;

import ua.staff.model.Person;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TestEntityManager entityManager;

    private List<Person> people;

    @BeforeEach
    void setUp(){
        people = PersonGenerator.generatePeople(5);
        people.forEach(entityManager::persist);
        people.sort(Comparator.comparing(Person::getCreated_at));
        entityManager.flush();
    }

    @Test
    void findAllPeopleTest(){
        var pageable = PageRequest.of(0,people.size());
        var peopleDtoSlice = personRepository.findAllPeople(pageable);
        var listPeople = peopleDtoSlice.getContent();

        assertThat(people.get(0).getEmail()).isEqualTo(listPeople.get(0).numberPhone());
        assertThat(people.get(1).getEmail()).isEqualTo(listPeople.get(1).numberPhone());
    }

    @Test
    void findPersonDtoByIdTest(){
        var person = people.get(1);
        var personDto = personRepository.findPersonDtoById(people.get(1).getId())
                .orElseThrow();

        assertThat(personDto.numberPhone()).isEqualTo(person.getEmail());
        assertThat(personDto.id()).isEqualTo(person.getId());
    }


    @Test
    void findPersonFetchBasketByIdTest(){
        var person = people.get(0);
        person.getBasket().setUsedBonuses(valueOf(20));
        entityManager.flush();

        var foundPerson = personRepository.findPersonFetchBasketById(person.getId())
                .orElseThrow();

        assertThat(foundPerson.getId()).isEqualTo(person.getId());
        assertThat(foundPerson.getBasket().getTotalPrice())
                .isEqualTo(person.getBasket().getTotalPrice());
    }

    @Test
    void findPersonFetchBasketAndChoseClothesById(){
        var person = people.get(2);
        var choseClothes = ChoseClothesGenerator.genereteChoseClothes(2);
        person.getBasket().setUsedBonuses(valueOf(40));
        person.getBasket().addClothesToBasket(choseClothes);
        entityManager.flush();

        var foundPerson = personRepository.findPersonFetchBasketAndChoseClothesById(person.getId())
                        .orElseThrow();

        assertThat(foundPerson.getBasket().getUsedBonuses())
                .isEqualTo(person.getBasket().getUsedBonuses());

        assertThat(foundPerson.getBasket().getChoseClothes().get(0).getId())
                .isEqualTo(person.getBasket().getChoseClothes().get(0).getId());

    }

    @Test
    void findPostAddressByIdTest(){
        var person = people.get(2);
        var postAddress = personRepository.findPostAddressById(person.getId())
                .orElseThrow();

        assertThat(postAddress.getCity()).isEqualTo(person.getPostAddress().getCity());
        assertThat(postAddress.getPostOffice()).isEqualTo(person.getPostAddress().getPostOffice());
    }

    @Test
    void findPersonBonusesByIdTest(){
        var person = people.get(2);
        var personBonuses = personRepository.findPersonBonusesById(person.getId())
                .orElseThrow();

        assertThat(personBonuses.setScale(0)).isEqualTo(person.getBonuses());
    }

    @Test
    void updatePersonBonusesByIdTest(){
        var person = people.get(0);
        personRepository.updatePersonBonusesById(valueOf(125), person.getId());
        personRepository.flush();

        var updatedBonuses = entityManager.getEntityManager()
                .createQuery("select p.bonuses from Person p where p.id =?1", BigDecimal.class)
                .setParameter(1,person.getId())
                .getSingleResult();

        assertThat(valueOf(125)).isEqualTo(updatedBonuses.setScale(0));
    }
}
