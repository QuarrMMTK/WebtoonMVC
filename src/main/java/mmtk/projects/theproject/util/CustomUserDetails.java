package mmtk.projects.theproject.util;

import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Author : Min Myat Thu Kha
 * Created At : 29/11/2024, Nov , 09:21
 * Project Name : WebtoonMVC
 **/
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Return the password from the User entity
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Assuming you want to use email as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can implement this logic as per your needs
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement logic if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement logic if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // You can modify this based on the user's activation status
    }
}
