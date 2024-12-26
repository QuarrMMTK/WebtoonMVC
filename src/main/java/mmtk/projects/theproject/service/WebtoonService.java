package mmtk.projects.theproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.dto.GenreDto;
import mmtk.projects.theproject.model.Genre;
import mmtk.projects.theproject.model.Webtoon;
import mmtk.projects.theproject.repository.GenreRepository;
import mmtk.projects.theproject.repository.WebtoonRepository;
import mmtk.projects.theproject.util.FileUploadUtil;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
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
    private final GenreRepository genreRepository;

    public List<Webtoon> findAllWebtoons(int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        return webtoonRepository.findAll(pageable).getContent(); // This works out of the box.
    }


    public String createWebtoon(Webtoon webtoon, Authentication connectedUser, MultipartFile file) throws IOException {
        if (!connectedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Only admins can create webtoons.");
        }
        Webtoon webtoonSaved = new Webtoon();
        webtoonSaved.setUploadedDate(new Date());
        webtoonSaved.setModificationDate(new Date());
        webtoonSaved.setStatus(webtoon.getStatus());
        webtoonSaved.setGenres(webtoon.getGenres());
        webtoonSaved.setAuthor(webtoon.getAuthor());
        webtoonSaved.setSynopsis(webtoon.getSynopsis());
        webtoonSaved.setTitle(webtoon.getTitle());

        // Define the upload directory
        String uploadDir = "webtoon/webtoon-covers/" + webtoon.getTitle().replace(" ", "-").toLowerCase() + "/";

        // Save the file and get the saved file path
        String savedFilePath = FileUploadUtil.saveFile(uploadDir, file.getOriginalFilename(), file);

        // Set the file path to the webtoon entity
        webtoonSaved.setCoverImage("webtoon-covers/" + webtoon.getTitle().replace(" ", "-").toLowerCase() + "/" + file.getOriginalFilename());

        // Save the webtoon to the database

        Webtoon savedWebtoon = webtoonRepository.save(webtoonSaved);

        return savedWebtoon.getTitle();
    }
    public String editWebtoon(long webtoonId, Webtoon webtoon, Authentication connectedUser, MultipartFile file) throws IOException {
        if (connectedUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            // Fetch the existing Webtoon from the database
            Webtoon existingWebtoon = webtoonRepository.findById(webtoonId)
                    .orElseThrow(() -> new EntityNotFoundException("Webtoon not found"));

            // Update the fields of the existing webtoon with the new values from the input
            existingWebtoon.setTitle(webtoon.getTitle());
            existingWebtoon.setAuthor(webtoon.getAuthor());
            existingWebtoon.setSynopsis(webtoon.getSynopsis());
            existingWebtoon.setModificationDate(new Date());  // Update the modification date
            existingWebtoon.setStatus(webtoon.getStatus());

            // Keep the existing genres intact by not overwriting them
            // existingWebtoon.setGenres(webtoon.getGenres()); <-- Remove this line

            // Handle cover image file upload if a new file is provided
            if (file != null && !file.isEmpty()) {
                // Define the folder and filename
                String folderName = webtoon.getTitle().replace(" ", "-").toLowerCase();
                String uploadDir = "webtoon/webtoon-covers/" + folderName + "/";

                // Save the new file and get the saved file path
                String savedFilePath = FileUploadUtil.saveFile(uploadDir, file.getOriginalFilename(), file);

                // Set the new cover image path in the Webtoon entity
                existingWebtoon.setCoverImage("webtoon-covers/" + folderName + "/" + file.getOriginalFilename());
            }

            // Save the updated webtoon to the database
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
        return webtoonRepository.findByTitle(webtoonName);
    }


    /**
     * Add a new genre to the database
     *
     * @param genreDto the data transfer object containing genre details
     */
    public void addGenre(GenreDto genreDto) {
        // Convert GenreDto to Genre entity
        Genre genre = new Genre();
        genre.setName(genreDto.getName());
        genre.setDescription(genreDto.getDescription());
        // Save the genre to the repository
        genreRepository.save(genre);
    }
}