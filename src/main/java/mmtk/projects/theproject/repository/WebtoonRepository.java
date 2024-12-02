package mmtk.projects.theproject.repository;

import mmtk.projects.theproject.model.Webtoon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;

/**
 * Author : Min Myat Thu Kha
 * Created At : 05/11/2024, Nov ,11, 47
 * Project Name : WebtoonMVC
 **/
@Repository
public interface WebtoonRepository extends JpaRepository<Webtoon, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Webtoon w SET w = :webtoon WHERE w.id = :webtoonId")
    void editWebtoon(@Param("webtoonId") long webtoonId, @Param("webtoon") Webtoon webtoon);

    Page<Webtoon> findAllByOrderByCreationDateDesc(Pageable pageable);
    @Query("SELECT w FROM Webtoon w ORDER BY w.creationDate DESC")
    Page<Webtoon> findAllWebtoons(Pageable pageable); // Custom query
}
