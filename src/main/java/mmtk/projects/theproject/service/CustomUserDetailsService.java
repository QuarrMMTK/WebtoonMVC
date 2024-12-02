package mmtk.projects.theproject.service;

import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.model.User;
import mmtk.projects.theproject.repository.UserRepository;
import mmtk.projects.theproject.util.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Author : Min Myat Thu Kha
 * Created At : 28/11/2024, Nov , 09:33
 * Project Name : WebtoonMVC
 **/
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user); // Return the CustomUserDetails object
    }
}
