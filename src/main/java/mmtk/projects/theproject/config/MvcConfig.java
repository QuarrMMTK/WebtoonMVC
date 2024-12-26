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
        exposeDirectory("photos/profiles", registry);
        exposeDirectory("webtoon/webtoon-covers", registry);
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName).toAbsolutePath().normalize();
        String uploadDirPath = uploadDir.toUri().toString();

        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations(uploadDirPath);
    }
}
