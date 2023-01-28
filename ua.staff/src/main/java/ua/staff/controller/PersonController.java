package ua.staff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.staff.builder.UriBuilder;
import ua.staff.dto.PeopleDto;
import ua.staff.dto.PersonDto;
import ua.staff.model.Person;
import ua.staff.model.PostAddress;
import ua.staff.service.PersonService;

import static org.springframework.http.HttpStatus.*;
import static ua.staff.generator.ResponseEntityGenerator.*;


@RestController
@RequestMapping("/api/people")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping
    @PreAuthorize("hasAuthority('advanced')")
    public Slice<PeopleDto> getAll(Pageable pageable){
        return personService.getAllPeople(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('advanced') or authentication.principal.id == #id")
    public PersonDto getPerson(@PathVariable("id")Long id){
        return personService.getPersonById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('advanced')")
    public ResponseEntity<Person> savePerson(@RequestBody Person person){
        personService.savePerson(person);

        var location = UriBuilder.createUriFromCurrentServletMapping("people/{id}",person.getId());
        return getResponseEntity(location, CREATED,person);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('advanced') or authentication.principal.id == #id")
    public ResponseEntity<Void> updatePerson(@PathVariable("id")Long id, @RequestBody Person person){
        personService.updatePerson(id,person);

        var location = UriBuilder.createUriFromCurrentRequest();
        return getResponseEntityWithNoContent(location);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('advanced') or authentication.principal.id == #id")
    public ResponseEntity<Person> deletePerson(@PathVariable("id")Long id) {
        var person = personService.deletePerson(id);

        var location = UriBuilder.createUriFromCurrentServletMapping("/people");
        return getResponseEntity(location, CREATED,person);
    }

    @GetMapping("/{id}/address")
    @PreAuthorize("hasAuthority('advanced') or authentication.principal.id == #id")
    public PostAddress getAddress(@PathVariable Long id){
        return personService.getPostAddressById(id);
    }
}

