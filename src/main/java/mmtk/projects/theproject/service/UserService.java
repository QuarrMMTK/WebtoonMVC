package mmtk.projects.theproject.service;

import lombok.RequiredArgsConstructor;
import mmtk.projects.webtoonmvc.dto.UserRegistrationDto;
import mmtk.projects.theproject.model.Role;
import mmtk.projects.theproject.model.User;
import mmtk.projects.theproject.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Author : Min Myat Thu Kha
 * Created At : 28/11/2024, Nov , 09:51
 * Project Name : WebtoonMVC
 **/
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void addUser(UserRegistrationDto userDto, String photoName) {
        // Create a new User object
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setCreatedAt(new Date(System.currentTimeMillis()));
        newUser.setEmail(userDto.getEmail());
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setRole(Role.ADMIN);
        if (photoName != null && !photoName.isEmpty()) {
            newUser.setProfilePhoto(photoName);
        }
        System.out.println(newUser);
        userRepository.save(newUser);
        System.out.println("User saved to DB");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public long getUserCount() {
        System.out.println(userRepository.count());
        System.out.println("Users count" + userRepository.getUserCount());
        return userRepository.getUserCount();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getName();
        }
        return null;
    }

    public User getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User is not registered"));
        }
        return null;
    }
}
