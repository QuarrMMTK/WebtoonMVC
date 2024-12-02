package mmtk.projects.theproject.config;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Author : Min Myat Thu Kha
 * Created At : 01/12/2024, Dec , 21:52
 * Project Name : WebtoonMVC
 **/
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        exposeDirectory(registry);
    }

    private void exposeDirectory(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("photos/profiles").toAbsolutePath().normalize(); // Normalize and convert to absolute path
        String uploadDirPath = uploadDir.toUri().toString(); // Convert to URI format

        registry.addResourceHandler("/" + "photos/profiles" + "/**") // Map the URL path
                .addResourceLocations(uploadDirPath); // Serve files from the directory
    }
}
