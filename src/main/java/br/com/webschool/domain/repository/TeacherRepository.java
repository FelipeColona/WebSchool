package br.com.webschool.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.webschool.domain.model.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByName(String name);
    Optional<Teacher> findByLogin(String login);

    @Query(
        value = "SELECT * FROM teacher WHERE name LIKE %?1%", 
        nativeQuery = true
    )
    List<Teacher> findByPartialMatching(String name);

/*     @Query(
        value = "SELECT * FROM teacher WHERE name LIKE %?1%", 
        countQuery = "SELECT count(*) FROM teacher WHERE name LIKE %?1%",
        nativeQuery = true
    )
    Page<Teacher> teste(String name, Pageable pageable); */
}
