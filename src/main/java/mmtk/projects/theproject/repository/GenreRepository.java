package mmtk.projects.theproject.repository;

import mmtk.projects.theproject.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author : Min Myat Thu Kha
 * Created At : 03/12/2024, Dec , 08:49
 * Project Name : TheProject
 **/
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
