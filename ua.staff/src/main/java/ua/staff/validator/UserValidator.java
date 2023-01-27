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

        checkIsUserPresent(user,errors);
        checkIsCorrectPassword(user,errors);

    }

    private void checkIsUserPresent(UserForm user,Errors errors){
        boolean isPresent = personRepository.findByEmail(user.getEmail()).isPresent();

        if (isPresent){
            errors.reject("email","Such user already exists");
        }
    }

    private void checkIsCorrectPassword(UserForm user, Errors errors) {
        if (!user.getPassword().equals(user.getConfirmPassword())){
            errors.reject("confirm password","Password don't match");
        }
    }
}
