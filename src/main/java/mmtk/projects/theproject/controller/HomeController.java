package mmtk.projects.theproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Author : Min Myat Thu Kha
 * Created At : 28/11/2024, Nov , 09:49
 * Project Name : WebtoonMVC
 **/
@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "/login";
    }
    @GetMapping("/home")
    public String home(Model model) {
        return "/home";
    }
}
