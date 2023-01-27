package ua.staff.security.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ua.staff.model.Person;

import java.util.Collection;

public class SecurityUser extends User {
    private Long id;

    public SecurityUser(String username,
                        String password,
                        boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
                        boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Long id) {
        super(username, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }

    public static UserDetails fromUser(Person user) {
        return new SecurityUser(
                user.getEmail(), user.getPassword(),
                user.getStatus().equals("ACTIVE"),
                user.getStatus().equals("ACTIVE"),
                user.getStatus().equals("ACTIVE"),
                user.getStatus().equals("ACTIVE"),
                user.getRole().getAuthorities(),
                user.getId()
        );
    }
}
