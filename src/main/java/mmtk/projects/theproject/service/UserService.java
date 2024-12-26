package mmtk.projects.theproject.service;

import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.dto.CreateUserDto;
import mmtk.projects.theproject.dto.UserRegistrationDto;
import mmtk.projects.theproject.model.Role;
import mmtk.projects.theproject.model.User;
import mmtk.projects.theproject.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        newUser.setCreatedAt(new Date());
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
    public Page<User> getPaginatedUsers(int page, int pageSize) {
        return userRepository.findAll(PageRequest.of(page, pageSize));
    }

    public void createNewUser(CreateUserDto createUserDto, String fileName) {
        User user = new User();
        user.setFirstName(createUserDto.getFirstName());
        user.setLastName(createUserDto.getLastName());
        user.setCreatedAt(new Date());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user.setProfilePhoto(fileName); // Assuming fileName is saved earlier
        user.setRole(Role.valueOf(createUserDto.getRole()));
        userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    public void updateUserProfile(Long id,User user, String fileName) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        System.out.println("Service Method : " + existingUser);
        if (!fileName.isEmpty()) {
            existingUser.setProfilePhoto(fileName);
            existingUser.setCreatedAt(new Date());
            existingUser.setEmail(user.getEmail());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            userRepository.save(existingUser);
            System.out.println("Service Method With Image: " + existingUser);
        }else {
            existingUser.setCreatedAt(new Date());
            existingUser.setEmail(user.getEmail());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            System.out.println(existingUser);
            userRepository.save(existingUser);
            System.out.println("Service Method Without Image: " + existingUser);
        }

    }
    public boolean validateCurrentPassword(User currentUser, String currentPassword) {
        return passwordEncoder.matches(currentPassword, currentUser.getPassword());
    }
    public void updatePassword(User currentUser, String newPassword) {
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
    }
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public Page<User> search(String keyword, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        // If pageSize is default (10), return all results without pagination
        if (page == 0 && pageSize == 10) {
            List<User> users = userRepository.search(keyword);
            return new PageImpl<>(users, pageable, users.size());
        }
        return userRepository.search(keyword, pageable);
    }
}
