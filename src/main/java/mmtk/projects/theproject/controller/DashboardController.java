package mmtk.projects.theproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mmtk.projects.theproject.dto.UserRegistrationDto;
import mmtk.projects.theproject.model.User;
import mmtk.projects.theproject.service.ChapterService;
import mmtk.projects.theproject.service.UserService;
import mmtk.projects.theproject.service.WebtoonSerivce;
import mmtk.projects.theproject.util.FileUploadUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
    private final WebtoonSerivce webtoonSerivce;
    private final ChapterService episodeService;
    private final ChapterService chapterService;

    @GetMapping("/dashboard")
    public String goToDashboard(Model model) {
        User user = userService.getCurrentUserProfile(); // PROFILE
        Long webtoonCount = webtoonSerivce.getWebtoonCount();
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

    @PostMapping("/dashboard/User/addnew")
    public String addNewUser(@ModelAttribute("user") User user, @RequestParam("image") MultipartFile file, Model model) {
        User currentUser = userService.getCurrentUserProfile();
        String fileName = "";
        if (!file.isEmpty()) {
            fileName = file.getOriginalFilename();
            currentUser.setProfilePhoto(fileName);
        }

        String uploadDir = "photos/profiles/" + currentUser.getId();
        try {
            FileUploadUtil.saveFile(uploadDir, fileName, file);
        } catch (IOException e) {
            logger.error("Error occurred while saving file for user {}: {}", currentUser.getId(), e.getMessage(), e);
        }
        return "redirect:/dashboard/";
    }

    @GetMapping("/user/add-user")
    public String goToAddUser(Model model) {
        User user = userService.getCurrentUserProfile();
        model.addAttribute("user", user);
        model.addAttribute("newUser", new UserRegistrationDto());
        return "add-user";
    }
}
