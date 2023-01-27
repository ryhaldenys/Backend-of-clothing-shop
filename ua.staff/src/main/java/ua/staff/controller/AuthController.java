package ua.staff.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.staff.dto.UserForm;

import ua.staff.service.PersonService;
import ua.staff.validator.UserValidator;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PersonService userService;
    private final UserValidator userValidator;

    @GetMapping("/login")
    public String getLoginPage(String error, Model model){
        if (error != null)
            model.addAttribute("error","email or password is incorrect.");

        return "login";
    }

    @GetMapping("/logout")
    public String getLogoutPage(){
        return "new";
    }


    @GetMapping("/registration")
    public String register(org.springframework.ui.Model model){
        model.addAttribute("user", new UserForm());

        return "registration";
    }

    @PostMapping("/registration")
    public String userRegistration(@ModelAttribute("user") @Valid UserForm user,BindingResult bindingResult){
        userValidator.validate(user,bindingResult);

        if (bindingResult.hasErrors()){
            return "registration";
        }

        userService.savePerson(user);
        return "redirect:logout";
    }

}

