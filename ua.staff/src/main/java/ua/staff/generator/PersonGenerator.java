package ua.staff.generator;

import ua.staff.builder.PersonBuilder;
import ua.staff.model.Basket;
import ua.staff.model.Person;
import ua.staff.model.PostAddress;
import ua.staff.model.Role;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PersonGenerator {

    public static List<Person> generatePeople(int count){
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            var person = generatePerson(i);
            people.add(person);
        }
        return people;
    }

    public static Person generatePerson(int index){
        Person person =  PersonBuilder.builder()
                .firstName("firstName"+index)
                .lastName("lastName"+index)
                .numberPhone("numberPhone"+index)
                .roles(List.of(new Role("USER")))
                .password("password"+index)
                .bonuses(BigDecimal.valueOf(index))
                .postAddress(generatePostAddress(index))
                .build();
        person.addBasket(new Basket());
        return person;
    }

    public static PostAddress generatePostAddress(int index){
        var postAddress = new PostAddress();
        postAddress.setCity("City"+index);
        postAddress.setPostOffice("postOffice"+index);
        postAddress.setCityJustin("CityJastin"+index);
        postAddress.setJustinOffice("JastinOffice"+index);
        return postAddress;
    }
}
