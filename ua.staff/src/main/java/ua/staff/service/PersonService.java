package ua.staff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.staff.dto.PeopleDto;
import ua.staff.dto.PersonDto;
import ua.staff.dto.UserForm;
import ua.staff.exception.NotFoundException;
import ua.staff.model.Basket;
import ua.staff.model.Person;
import ua.staff.model.PostAddress;
import ua.staff.model.Role;
import ua.staff.repository.PersonRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;


    public Slice<PeopleDto> getAllPeople(Pageable pageable){
        var people = getPeople(pageable);
        checkIsNotEmpty(people);
        return people;
    }

    private Slice<PeopleDto> getPeople(Pageable pageable){
        return personRepository.findAllPeople(pageable);
    }

    private void checkIsNotEmpty(Slice<PeopleDto> people) {
        if (people.getContent().isEmpty())
            throw new NotFoundException("Cannot find any people");
    }



    public PersonDto getPersonById(Long id){
        return getPersonDtoById(id);
    }

    private PersonDto getPersonDtoById(Long id){
        return personRepository.findPersonDtoById(id)
                .orElseThrow(()->new NotFoundException("Cannot find person by id:"+id));
    }


    @Transactional
    public void updatePerson(Long id, Person person){
        var foundPerson = findPersonById(id);
        updatePerson(foundPerson,person);
    }

    private Person findPersonById(Long id){
        return personRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot find person by id: "+id));
    }

    private void updatePerson(Person foundPerson, Person person) {
        foundPerson.setFirstName(person.getFirstName());
        foundPerson.setLastName(person.getLastName());
        foundPerson.setEmail(person.getEmail());
        foundPerson.setPostAddress(person.getPostAddress());
    }


    @Transactional
    public Person savePerson(UserForm user) {
        System.out.println(user);
        Person person = new Person();
        person.setFirstName(user.getFirstName());
        person.setLastName(user.getLastName());
        person.setEmail(user.getEmail());
        person.setPassword(passwordEncoder.encode(user.getPassword()));
        person.setRole(Role.USER);
        person.setStatus("ACTIVE");
        addBasket(person);
        return save(person);
    }

    @Transactional
    public Person savePerson(Person person) {
        return save(person);
    }


    private void addBasket(Person person){
        person.addBasket(new Basket());
    }

    private Person save(Person person){
        return personRepository.save(person);
    }


    public Person deletePerson(Long id) {
        return deleteById(id);
    }

    private Person deleteById(Long id){
        var person = personRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot find person by id: "+id));
        personRepository.deleteById(id);
        return person;
    }


    public PostAddress getPostAddressById(Long id) {
        return personRepository.findPostAddressById(id)
                .orElseThrow(()->new NotFoundException("Cannot find person's post adress by id: "+id));
    }
}
