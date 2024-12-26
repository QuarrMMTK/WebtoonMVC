package mmtk.projects.theproject.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Author : Min Myat Thu Kha
 * Created At : 28/11/2024, Nov , 09:43
 * Project Name : WebtoonMVC
 **/
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String username = user.getUsername();
        System.out.println("Logged in as: " + username);

        boolean hasAdminRole = authentication.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
        boolean hasUserRole = authentication.getAuthorities().stream()
                .anyMatch(g -> g.getAuthority().equals("ROLE_USER"));

        System.out.println("Roles: " + authentication.getAuthorities().toString());

        if (hasAdminRole) {
            // Redirect to admin dashboard
            response.sendRedirect("/dashboard");
        } else if (hasUserRole) {
            // Redirect to user home page
            response.sendRedirect("/home");
        } else {
            // Redirect to default page if no roles match
            response.sendRedirect("/login");
        }
    }
}
