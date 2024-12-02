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
        // Map "/photos/profiles/**" to the physical directory "photos/profiles"
        exposeDirectory("photos/profiles", registry);

        // Map "/webtoon-covers/**" to the physical directory "webtoon/webtoon-covers"
        exposeDirectory("webtoon/webtoon-covers", registry);
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName).toAbsolutePath().normalize(); // Get absolute path
        String uploadDirPath = uploadDir.toUri().toString(); // Convert to URI format

        registry.addResourceHandler("/" + dirName + "/**") // Map the URL path
                .addResourceLocations(uploadDirPath); // Serve files from the directory
    }
}
