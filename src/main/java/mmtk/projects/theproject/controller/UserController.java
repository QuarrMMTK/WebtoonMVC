package mmtk.projects.theproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.dto.UserRegistrationDto;
import mmtk.projects.theproject.model.User;
import mmtk.projects.theproject.service.UserService;
import mmtk.projects.theproject.util.FileUploadUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Author : Min Myat Thu Kha
 * Created At : 28/11/2024, Nov , 10:03
 * Project Name : WebtoonMVC
 **/
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register/new")
    public String register(@ModelAttribute @Valid UserRegistrationDto userRegistrationDTO,
                           BindingResult result,
                           Model model,
                           @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture) throws IOException {

        if (result.hasErrors()) {
            System.out.println("Binding errors: " + result.getAllErrors());
            return "register";
        }

        if (!userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match!");
            return "register";
        }

        Optional<User> user = userService.getUserByEmail(userRegistrationDTO.getEmail());
        if (user.isPresent()) {
            model.addAttribute("error", "Email address already in use!");
            return "register";
        }

        if (profilePicture != null && !profilePicture.isEmpty()) {
            // Define the upload directory relative to the project directory
            String uploadDir = "photos/profiles";

            // Sanitize and clean the file name
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(profilePicture.getOriginalFilename()));

            // Create directories if not already present
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            // Save the file
            try {
                FileUploadUtil.saveFile(uploadDir, fileName, profilePicture);
            } catch (IOException e) {
                model.addAttribute("error", "Error uploading the profile picture. Please try again.");
                return "register";
            }

            // Pass the file name to the user registration service
            userService.addUser(userRegistrationDTO, fileName);
        } else {
            userService.addUser(userRegistrationDTO, null); // Handle case with no profile picture
        }

        return "redirect:/login";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login";
    }


}
