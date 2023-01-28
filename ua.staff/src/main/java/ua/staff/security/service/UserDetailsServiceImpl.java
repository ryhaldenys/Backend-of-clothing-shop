package ua.staff.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.staff.model.Person;
import ua.staff.repository.PersonRepository;
import ua.staff.security.model.SecurityUser;

@Service("userDetailsServiceImpl")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final PersonRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person user = userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User doesn`t exsist"));
        return SecurityUser.fromUser(user);
    }
}
