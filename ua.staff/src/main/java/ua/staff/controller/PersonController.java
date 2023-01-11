package ua.staff.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.staff.builder.UriBuilder;
import ua.staff.dto.PeopleDto;
import ua.staff.dto.PersonRolesDto;
import ua.staff.model.Person;
import ua.staff.service.PersonService;


@RestController
@RequestMapping("/people")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public Slice<PeopleDto> getAll(Pageable pageable){
        return personService.getAllPeople(pageable);
    }

    @GetMapping("/{id}")
    public PersonRolesDto getPerson(@PathVariable("id")Long id){
        return personService.getPersonById(id);
    }

    @PostMapping
    public ResponseEntity<Person> savePerson(@RequestBody Person person){
        personService.savePerson(person);

        var location = UriBuilder.createUriFromCurrentServletMapping("people/{id}",person.getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location)
                .body(person);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable("id")Long id, @RequestBody Person person){
        personService.updatePerson(id,person);

        var location = UriBuilder.createUriFromCurrentRequest();

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(location)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable("id")Long id) {
        var person = personService.deletePerson(id);
        var location = UriBuilder.createUriFromCurrentServletMapping("/people");

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(location)
                .body(person);
    }
}
