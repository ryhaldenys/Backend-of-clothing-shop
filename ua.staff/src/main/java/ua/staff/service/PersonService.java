package ua.staff.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.staff.dto.PeopleDto;
import ua.staff.dto.PersonRolesDto;
import ua.staff.exception.NotFoundException;
import ua.staff.model.Basket;
import ua.staff.model.Person;
import ua.staff.repository.PersonRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;

    public Slice<PeopleDto> getAllPeople(Pageable pageable){
        var people = personRepository.findAllPeople(pageable);
        checkIsNotEmpty(people);
        return people;
    }

    private void checkIsNotEmpty(Slice<PeopleDto> people) {
        if (people.isEmpty())
            throw new NotFoundException("Cannot find any people");
    }

    public PersonRolesDto getPersonById(Long id){
        return createPersonRolesDto(id);
    }

    private PersonRolesDto createPersonRolesDto(Long id) {
        var personDto = personRepository.findPersonDtoById(id)
                .orElseThrow(()->new NotFoundException("Cannot find person by id:"+id));
        var roles = personRepository.findPersonRolesById(id);

        return new PersonRolesDto(personDto,roles);
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
        foundPerson.setNumberPhone(person.getNumberPhone());
        foundPerson.setPostAddress(person.getPostAddress());
    }

    @Transactional
    public Person savePerson(Person person) {
        person.addBasket(new Basket());
        return personRepository.save(person);
    }
}
