package br.com.webschool.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.webschool.domain.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByName(String name);
    Optional<Student> findByLogin(String login);

    @Query(
        value = "SELECT * FROM student WHERE name LIKE %?1%", 
        nativeQuery = true
    )
    List<Student> findByPartialMatching(String name);
}
