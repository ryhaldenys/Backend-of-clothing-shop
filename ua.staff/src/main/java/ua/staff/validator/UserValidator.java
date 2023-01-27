package ua.staff.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.staff.dto.UserForm;
import ua.staff.repository.PersonRepository;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final PersonRepository personRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserForm user = (UserForm) target;

        boolean isPresent = personRepository.findByEmail(user.getEmail()).isPresent();

        System.out.println(isPresent);
        if (isPresent){
            errors.reject("email","Such user already exists");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())){
            errors.reject("confirm password","Password don't match");
        }
    }
}
