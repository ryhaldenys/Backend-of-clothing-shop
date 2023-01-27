package ua.staff.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserForm {

    @Size(min = 2,max = 30,message = "First name should be between 2 and 30 characters")
    private String firstName;

    @Size(min = 2,max = 30,message ="Last name should be between 2 and 30 characters")
    private String lastName;

    @Email(message = "Invalid email address")
    private String email;

    @Size(min = 8,max = 30,message ="Password should be between 8 and 30 characters")
    private String password;

    private String confirmPassword;
}
