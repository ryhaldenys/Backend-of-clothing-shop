package ua.staff.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import ua.staff.dto.PeopleDto;
import ua.staff.dto.PersonDto;
import ua.staff.dto.UserForm;
import ua.staff.model.Person;
import ua.staff.model.PostAddress;

public interface PersonService {
    Slice<PeopleDto> getAllPeople(Pageable pageable);
    PersonDto getPersonById(Long id);
    void updatePerson(Long id, Person person);
    Person savePerson(UserForm user);
    Person savePerson(Person person);
    Person deletePerson(Long id);
    PostAddress getPostAddressById(Long id);
}
