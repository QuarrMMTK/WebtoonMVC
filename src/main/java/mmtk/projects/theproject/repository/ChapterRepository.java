package mmtk.projects.theproject.repository;

import mmtk.projects.theproject.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author : Min Myat Thu Kha
 * Created At : 05/11/2024, Nov ,11, 47
 * Project Name : WebtoonMVC
 **/
@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
}
