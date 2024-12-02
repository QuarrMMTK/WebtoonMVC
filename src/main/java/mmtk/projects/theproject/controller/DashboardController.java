package mmtk.projects.theproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mmtk.projects.theproject.dto.CreateUserDto;
import mmtk.projects.theproject.model.User;
import mmtk.projects.theproject.service.ChapterService;
import mmtk.projects.theproject.service.UserService;
import mmtk.projects.theproject.service.WebtoonService;
import mmtk.projects.theproject.util.FileUploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Author : Min Myat Thu Kha
 * Created At : 29/11/2024, Nov , 11:45
 * Project Name : WebtoonMVC
 **/
@Controller
@Slf4j
@RequiredArgsConstructor
public class DashboardController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserService userService;
    private final WebtoonService webtoonService;
    private final ChapterService episodeService;
    private final ChapterService chapterService;

    @GetMapping("/dashboard")
    public String goToDashboard(Model model) {
        User user = userService.getCurrentUserProfile(); // PROFILE
        Long webtoonCount = webtoonService.getWebtoonCount();
        Long chapterCount = chapterService.getChapterCount();
        Long userCount = userService.getUserCount();
//      Send Attribute
        System.out.println("Profile Photo URL: " + user.getProfilePhoto());
        model.addAttribute("user", user);
        model.addAttribute("webtoonCount", webtoonCount);
        model.addAttribute("chapterCount", chapterCount);
        model.addAttribute("userCount", userCount);
        return "dashboard";
    }

    @GetMapping("/user/user-list")
    public String goToUserList(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        int pageSize = 10;
        Page<User> userPage = userService.getPaginatedUsers(page, pageSize);
        model.addAttribute("userPage", userPage);
        model.addAttribute("user", userService.getCurrentUserProfile());
        return "user-list";
    }

    @PostMapping("/user/add-user/new")
    public String addNewUser(@ModelAttribute("user") CreateUserDto createUserDto, @RequestParam("image") MultipartFile profilePicture, Model model) throws IOException {
        User currentUser = userService.getCurrentUserProfile();
        if (profilePicture != null && !profilePicture.isEmpty()) {
            // Define the upload directory relative to the project directory
            String uploadDir = "photos/profiles";

            // Sanitize and clean the file name
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(profilePicture.getOriginalFilename()));
            fileName = FileUploadUtil.sanitizeFileName(fileName);

            // Create directories if not already present
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            // Save the file
            try {
                FileUploadUtil.saveFile(uploadDir, fileName, profilePicture);
            } catch (IOException e) {
                model.addAttribute("error", "Error uploading the profile picture. Please try again.");
                return "redirect:/add-user";
            }

            // Pass the file name to the user registration service
            userService.createNewUser(createUserDto, fileName);
        } else {
            userService.createNewUser(createUserDto, null); // Handle case with no profile picture
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/user/add-user")
    public String goToAddUser(Model model) {
        model.addAttribute("user", userService.getCurrentUserProfile());
        model.addAttribute("createUserDto", new CreateUserDto());
        return "add-user";
    }

    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/user/user-list";
    }

    @GetMapping("/user/profile/{id}")
    public String viewUserProfile(@PathVariable Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user" , userService.getCurrentUserProfile()); // Session User
        model.addAttribute("currentUser", user); // For Showing User Info
        return "user-profile"; // The name of the Thymeleaf template for user profiles
    }

    @PostMapping("/user/profile/update")
    public String updateUserProfile(@ModelAttribute("user") User updatedUser,
                                    @RequestParam("profileImage") MultipartFile profileImage,
                                    RedirectAttributes redirectAttributes) {
        System.out.println(updatedUser);
            User currentUser = userService.getCurrentUserProfile();
            String fileName = "";
            if (!profileImage.isEmpty()) {
                String uploadDir = "photos/profiles";
                fileName = StringUtils.cleanPath(Objects.requireNonNull(profileImage.getOriginalFilename()));
                fileName = FileUploadUtil.sanitizeFileName(fileName);
                Path uploadPath = Paths.get(uploadDir);
                userService.updateUserProfile(currentUser.getId(), updatedUser, fileName);
            }
        System.out.println("updateUserProfile Controller : " + updatedUser);
        userService.updateUserProfile(currentUser.getId(), updatedUser, fileName);
        return "redirect:/dashboard";
    }

    @PostMapping("/user/profile/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("renewPassword") String renewPassword,
                                 RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUserProfile();
        try {
            // Validate current password
            if (!userService.validateCurrentPassword(currentUser, currentPassword)) {
                redirectAttributes.addFlashAttribute("error", "Current password is incorrect.");
                return "redirect:/user/profile/" + currentUser.getId();
            }

            // Validate new password match
            if (!newPassword.equals(renewPassword)) {
                redirectAttributes.addFlashAttribute("error", "New passwords do not match.");
                return "redirect:/user/profile/" + currentUser.getId();
            }

            // Update the password
            userService.updatePassword(currentUser, newPassword);
            redirectAttributes.addFlashAttribute("success", "Password updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while changing the password.");
        }

        return "redirect:/user/profile/" + (currentUser != null ? currentUser.getId() : ""); // Fallback if null
    }


    @GetMapping("/faq")
    public String goToFaq(Model model) {
        model.addAttribute("user", userService.getCurrentUserProfile());
        return "faq";
    }

    @GetMapping("/contact")
    public String goToContact(Model model) {
        model.addAttribute("user", userService.getCurrentUserProfile());
        return "contact";
    }


}
