package mmtk.projects.theproject.util;

import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.model.User;
import mmtk.projects.theproject.service.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Author : Min Myat Thu Kha
 * Created At : 26/12/2024, Dec , 14:29
 * Project Name : TheProject
 **/
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttribute {

    private final UserService userService;

    @ModelAttribute("user")
    public User addCurrentUserToModel() {
        return userService.getCurrentUserProfile();
    }
}
