package mmtk.projects.theproject.controller;

import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.model.Webtoon;
import mmtk.projects.theproject.service.WebtoonSerivce;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Author : Min Myat Thu Kha
 * Created At : 05/11/2024, Nov ,11, 27
 * Project Name : WebtoonMVC
 **/
@Controller
@RequestMapping("/webtoon")
@RequiredArgsConstructor
public class WebtoonController {

    private final WebtoonSerivce webtoonSerivce;

    @GetMapping
    public String findAllWebtoons(
            @RequestParam(name = "page" ,defaultValue = "0", required = false) int page,
            @RequestParam(name = "size" ,defaultValue = "10", required = false) int size,
            Authentication connectedUser,
            Model model) {
        List<Webtoon> webtoons = webtoonSerivce.findAllWebtoons(page, size, connectedUser);
        model.addAttribute("webtoons", webtoons);
        return "home";
    }

    @PostMapping("/create")
    public String createWebtoon(
            Webtoon webtoon,
            Authentication connectedUser,
            Model model) {
        String webtoonName = webtoonSerivce.createWebtoon(webtoon, connectedUser);
        model.addAttribute("webtoonName", webtoonName);
        return "redirect:/home";
    }

    @PutMapping("/edit/{webtoon-id}")
    public String editWebtoon(
            @RequestParam("webtoon-id") long webtoonId,
            Webtoon webtoon,
            Authentication connectedUser,
            Model model) {
        String webtoonName = webtoonSerivce.editWebtoon(webtoonId, webtoon, connectedUser);
        model.addAttribute("webtoonName", webtoonName);
        return "redirect:/home";
    }

    @DeleteMapping("/delete/{webtoon-id}")
    public String deleteWebtoon(@RequestParam("webtoon-id") long webtoonId,
                                Authentication connectedUser,
                                Model model) {
        String webtoonName = webtoonSerivce.deleteWebtoon(webtoonId, connectedUser);
        model.addAttribute("webtoonName", webtoonName);
        return "redirect:/home";
    }
}
