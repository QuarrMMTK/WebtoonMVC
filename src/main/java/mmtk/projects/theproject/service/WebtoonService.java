package mmtk.projects.theproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.dto.GenreDto;
import mmtk.projects.theproject.model.Webtoon;
import mmtk.projects.theproject.repository.WebtoonRepository;
import mmtk.projects.theproject.util.FileUploadUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Author : Min Myat Thu Kha
 * Created At : 05/11/2024, Nov ,11, 46
 * Project Name : WebtoonMVC
 **/
@Service
@RequiredArgsConstructor
public class WebtoonService {

    private final WebtoonRepository webtoonRepository;

    public List<Webtoon> findAllWebtoons(int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        return webtoonRepository.findAll(pageable).getContent(); // This works out of the box.
    }


    public String createWebtoon(Webtoon webtoon, Authentication connectedUser, MultipartFile file) throws IOException, IOException {
        if (!connectedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Only admins can create webtoons.");
        }

        // Define the upload directory
        String uploadDir = "webtoon/webtoon-covers/" + webtoon.getTitle() + "/";

        // Save the file and get the saved file path
        String savedFilePath = FileUploadUtil.saveFile(uploadDir, file.getOriginalFilename(), file);

        // Set the file path to the webtoon entity
        webtoon.setCoverImage("webtoon-covers/" + webtoon.getTitle() + "/" + file.getOriginalFilename());

        // Save the webtoon to the database
        Webtoon savedWebtoon = webtoonRepository.save(webtoon);

        return savedWebtoon.getTitle();
    }

    public String editWebtoon(long webtoonId, Webtoon webtoon, Authentication connectedUser) {
        if (connectedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            Webtoon existingWebtoon = webtoonRepository.findById(webtoonId)
                    .orElseThrow(() -> new EntityNotFoundException("Webtoon not found"));
            existingWebtoon.setTitle(webtoon.getTitle());
            existingWebtoon.setAuthor(webtoon.getAuthor());
            existingWebtoon.setSynopsis(webtoon.getSynopsis());
            existingWebtoon.setCoverImage(webtoon.getCoverImage());
            Webtoon updatedWebtoon = webtoonRepository.save(existingWebtoon);
            return updatedWebtoon.getTitle();
        } else {
            throw new AccessDeniedException("Only admins can edit webtoons.");
        }
    }

    public String deleteWebtoon(long webtoonId, Authentication connectedUser) {
        if (connectedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            Webtoon existingWebtoon = webtoonRepository.findById(webtoonId)
                    .orElseThrow(() -> new EntityNotFoundException("Webtoon not found"));
            webtoonRepository.delete(existingWebtoon);
            return existingWebtoon.getTitle();
        } else {
            throw new AccessDeniedException("Only admins can delete webtoons.");
        }
    }

    public Long getWebtoonCount() {
        return webtoonRepository.count();
    }

    public Optional<Webtoon> findById(long id){
        return webtoonRepository.findById(id);
    }

    public Webtoon getWebtoonById(Long id) {
        return webtoonRepository.findById(id).orElse(null);
    }

    public Webtoon findByTitle(String webtoonName) {

        return webtoonRepository.findByTitle(webtoonName).orElseThrow(() -> new EntityNotFoundException("Webtoon with title " + webtoonName + " not found"));
    }

    public void addGenre(GenreDto genreDto) {

    }
}