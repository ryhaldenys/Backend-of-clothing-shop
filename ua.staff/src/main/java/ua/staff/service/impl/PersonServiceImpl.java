package ua.staff.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.staff.builder.PersonBuilder;
import ua.staff.dto.PeopleDto;
import ua.staff.dto.PersonDto;
import ua.staff.dto.UserForm;
import ua.staff.exception.NotFoundException;
import ua.staff.model.Basket;
import ua.staff.model.Person;
import ua.staff.model.PostAddress;

import ua.staff.repository.PersonRepository;
import ua.staff.service.PersonService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;

    @Override
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


    @Override
    public PersonDto getPersonById(Long id){
        return getPersonDtoById(id);
    }

    private PersonDto getPersonDtoById(Long id){
        return personRepository.findPersonDtoById(id)
                .orElseThrow(()->new NotFoundException("Cannot find person by id:"+id));
    }


    @Override
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


    @Override
    @Transactional
    public Person savePerson(UserForm user) {
        var person = createPerson(user);
        return save(person);
    }

    private Person createPerson(UserForm user) {
        var person = PersonBuilder.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(encodePassword(user.getPassword()))
                .build();
        addBasket(person);
        return person;
    }

    @Override
    @Transactional
    public Person savePerson(Person person) {
        person.setPassword(encodePassword(person.getPassword()));
        return save(person);
    }

    private String encodePassword(String password){
        return passwordEncoder.encode(password);
    }

    private void addBasket(Person person){
        person.addBasket(new Basket());
    }

    private Person save(Person person){
        return personRepository.save(person);
    }

    @Override
    @Transactional
    public Person deletePerson(Long id) {
        return deleteById(id);
    }

    private Person deleteById(Long id){
        var person = personRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot find person by id: "+id));
        personRepository.deleteById(id);
        return person;
    }

    @Override
    public PostAddress getPostAddressById(Long id) {
        return personRepository.findPostAddressById(id)
                .orElseThrow(()->new NotFoundException("Cannot find person's post adress by id: "+id));
    }
}
