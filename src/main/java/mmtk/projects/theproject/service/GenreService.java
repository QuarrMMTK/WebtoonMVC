package mmtk.projects.theproject.service;

import lombok.RequiredArgsConstructor;
import mmtk.projects.theproject.model.Genre;
import mmtk.projects.theproject.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author : Min Myat Thu Kha
 * Created At : 03/12/2024, Dec , 08:49
 * Project Name : TheProject
 **/
@RequiredArgsConstructor
@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public List<Genre> findAllGenres() {
        return genreRepository.findAll();
    }
}
