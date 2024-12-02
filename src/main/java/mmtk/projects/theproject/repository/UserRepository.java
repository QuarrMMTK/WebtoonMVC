package mmtk.projects.theproject.repository;

import mmtk.projects.theproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author : Min Myat Thu Kha
 * Created At : 05/11/2024, Nov ,11, 48
 * Project Name : WebtoonMVC
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u")
    long getUserCount();
}
