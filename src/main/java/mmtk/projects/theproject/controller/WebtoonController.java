package mmtk.projects.theproject.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.dto.GenreDto;
import mmtk.projects.theproject.model.Chapter;
import mmtk.projects.theproject.model.User;
import mmtk.projects.theproject.model.Webtoon;
import mmtk.projects.theproject.service.ChapterService;
import mmtk.projects.theproject.service.GenreService;
import mmtk.projects.theproject.service.UserService;
import mmtk.projects.theproject.service.WebtoonService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * Author : Min Myat Thu Kha
 * Created At : 05/11/2024, Nov ,11, 27
 * Project Name : WebtoonMVC
 **/
@Controller
@RequiredArgsConstructor
public class WebtoonController {

    private final WebtoonService webtoonService;
    private final UserService userService;
    private final GenreService genreService;
    private final ChapterService chapterService;

    @GetMapping("/webtoon/webtoon-list")
    public String findAllWebtoons(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser,
            Model model) {
        // Add pagination data to the model
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (webtoonService.getWebtoonCount() / size) + 1);  // Basic pagination calculation
        model.addAttribute("webtoons", webtoonService.findAllWebtoons(page, size, connectedUser));
        model.addAttribute("user", userService.getCurrentUserProfile());
        return "webtoon-list";
    }

    @PostMapping("/webtoon/create")
    public String createWebtoon(
            @ModelAttribute Webtoon webtoon,
            Authentication connectedUser,
            Model model,
            @RequestParam("image") MultipartFile file
    ) {
        try {
            String webtoonName = webtoonService.createWebtoon(webtoon, connectedUser, file);
            model.addAttribute("successMessage", "Webtoon '" + webtoonName + "' created successfully!");
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error creating webtoon: " + e.getMessage());
        } catch (AccessDeniedException e) {
            model.addAttribute("errorMessage", "You don't have permission to create a webtoon.");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
        }
        return "redirect:/webtoon/webtoon-list"; // Or display the form with error messages
    }

    @PostMapping("/webtoon/edit/{webtoon-id}")
    public String editWebtoon(
            @PathVariable("webtoon-id") long webtoonId, // Use @PathVariable to capture the webtoon ID from the URL
            Webtoon webtoon,
            @RequestParam("file") MultipartFile file, // Add MultipartFile for file upload
            Authentication connectedUser,
            Model model) throws IOException { // Declare IOException to handle file upload exceptions

        // Call the service method to edit the webtoon
        String webtoonName = webtoonService.editWebtoon(webtoonId, webtoon, connectedUser, file);

        // Add the webtoon name to the model to pass to the view
        model.addAttribute("webtoonName", webtoonName);

        // Redirect to the dashboard or any other appropriate page after the update
        return "redirect:/webtoon/webtoon-list";
    }

    @PostMapping("/webtoon/delete/{id}")
    public String deleteWebtoon(@PathVariable("id") long webtoonId,
                                Authentication connectedUser,
                                Model model) {
        String webtoonName = webtoonService.deleteWebtoon(webtoonId, connectedUser);
        model.addAttribute("webtoonName", webtoonName);
        return "redirect:/dashboard";
    }

    @GetMapping("/webtoon/add-webtoon")
    public String addWebtoon(Model model) {
        model.addAttribute("user", userService.getCurrentUserProfile());
        model.addAttribute("webtoon", new Webtoon());
        model.addAttribute("allGenres", genreService.findAllGenres());
        return "add-webtoon";
    }

    @GetMapping("/webtoon/add-chapter/{id}")
    public String addChapter(Model model, @PathVariable long id) {
        model.addAttribute("user", userService.getCurrentUserProfile());
        model.addAttribute("webtoon", webtoonService.findById(id).orElseThrow(EntityNotFoundException::new));
        model.addAttribute("chapter", new Chapter());
        return "add-chapter";
    }

    @PostMapping("/webtoon/add-chapter/{id}")
    public String addChapter(
            @RequestParam("files") MultipartFile[] files,
            @ModelAttribute Chapter chapter,
            Authentication authentication,
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        // Retrieve the Webtoon or throw an exception
        Webtoon webtoon = webtoonService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Webtoon with ID " + id + " not found"));
        // Save the chapter with file uploads and other data
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("At least one file must be uploaded.");
        }
        chapterService.saveChapter(id, chapter.getName(), authentication, files);
        // Add success message for the user
        redirectAttributes.addFlashAttribute("message", "Chapter added successfully!");

        // Redirect to the appropriate page
        return "redirect:/webtoon/" + URLEncoder.encode(webtoon.getTitle(), StandardCharsets.UTF_8) + "/chapters";

    }


    @GetMapping("/webtoon/{title}")
    public String listChapters(
            @PathVariable String title,  // Extract the dynamic part of the URL
            Model model
    ) {
        User user = userService.getCurrentUserProfile();
        // Find the Webtoon by title
        System.out.println(title);
        title = URLDecoder.decode(title, StandardCharsets.UTF_8);
        System.out.println(title);
        Webtoon webtoon = webtoonService.findByTitle(title);
        System.out.println(webtoon.getTitle());
        // Retrieve the chapters associated with the Webtoon
        List<Chapter> chapters = chapterService.findChaptersByWebtoon(webtoon);
        // Add data to the model for rendering
        model.addAttribute("webtoon", webtoon);
        model.addAttribute("chapters", chapters);
        model.addAttribute("user", user);
        // Return the view name (e.g., a Thymeleaf or JSP template)
        return "chapter-list";
    }

    @GetMapping("/webtoon/add-genre")
    public String addGenre(Model model) {
        model.addAttribute("user", userService.getCurrentUserProfile());
        model.addAttribute("genreDto", new GenreDto());
        return "add-genre";
    }

    @PostMapping("/webtoon/add-new-genre")
    public String addGenre(@Valid @ModelAttribute GenreDto genreDto, BindingResult result, Model model) {
        // If validation fails, return to the same form with errors
        if (result.hasErrors()) {
            model.addAttribute("user", userService.getCurrentUserProfile());
            return "add-genre"; // Return to the add genre form page
        }

        // Call the service to handle the business logic
        webtoonService.addGenre(genreDto);

        // Redirect to the dashboard after successfully adding the genre
        return "redirect:/dashboard";
    }


    @GetMapping("/webtoon/webtoon-update/{id}")
    public String updateWebtoon(
            @PathVariable("id") long webtoonId,
            Model model
    ) {
        model.addAttribute("user", userService.getCurrentUserProfile());
        model.addAttribute("genre", genreService.findAllGenres());
        model.addAttribute("webtoon", webtoonService.findById(webtoonId).orElseThrow(EntityNotFoundException::new));
        return "webtoon-update";
    }
    @PostMapping("/webtoon/webtoon-update/{id}")
    public String updateWebtoon(
            @PathVariable("id") long webtoonId,
            @ModelAttribute Webtoon webtoon,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) throws IOException {
        webtoonService.editWebtoon(webtoonId, webtoon, authentication, file);
        return "redirect:/dashboard";
    }
}
