package mmtk.projects.theproject.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.dto.GenreDto;
import mmtk.projects.theproject.model.Chapter;
import mmtk.projects.theproject.model.User;
import mmtk.projects.theproject.model.Webtoon;
import mmtk.projects.theproject.service.ChapterService;
import mmtk.projects.theproject.service.GenreService;
import mmtk.projects.theproject.service.UserService;
import mmtk.projects.theproject.service.WebtoonService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
        List<Webtoon> webtoons = webtoonService.findAllWebtoons(page, size, connectedUser);
        // Add pagination data to the model
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (webtoonService.getWebtoonCount() / size) + 1);  // Basic pagination calculation
        model.addAttribute("webtoons", webtoons);
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
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating webtoon: " + e.getMessage());
        }

        return "redirect:/webtoon/webtoon-list";
    }

    @PutMapping("/webtoon/edit/{webtoon-id}")
    public String editWebtoon(
            @RequestParam("webtoon-id") long webtoonId,
            Webtoon webtoon,
            Authentication connectedUser,
            Model model) {
        String webtoonName = webtoonService.editWebtoon(webtoonId, webtoon, connectedUser);
        model.addAttribute("webtoonName", webtoonName);
        return "redirect:/dashboard";
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

    @PostMapping("/add-chapter/{id}")
    public String addChapter(
            @RequestParam("files") MultipartFile[] files,
            @ModelAttribute Chapter chapter,
            Authentication authentication,
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        // Retrieve the Webtoon or throw an exception
        Webtoon webtoon = webtoonService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Webtoon with ID " + id + " not found"));

        // Save the chapter with file uploads and other data
        chapterService.saveChapter(id, chapter.getName(), authentication, files);

        // Add success message for the user
        redirectAttributes.addFlashAttribute("message", "Chapter added successfully!");

        // Redirect to the appropriate page
        return "redirect:/webtoon/" + webtoon.getTitle() + "/chapters";
    }


    @GetMapping("/webtoon/{title}/chapters")
    public String listChapters(
            @PathVariable String title,  // Extract the dynamic part of the URL
            Model model
    ) {
        User user = userService.getCurrentUserProfile();
        // Find the Webtoon by title
        Webtoon webtoon = webtoonService.findByTitle(title);

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
        model.addAttribute("genre", new GenreDto());
        return "add-genre";
    }

    @PostMapping("/webtoon/add-new-genre")
    public String addGenre(@ModelAttribute GenreDto genreDto){
        webtoonService.addGenre(genreDto);
        return "redirect:/dashboard";
    }

}
