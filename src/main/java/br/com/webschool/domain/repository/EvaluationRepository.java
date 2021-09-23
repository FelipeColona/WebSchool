package br.com.webschool.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.webschool.domain.model.Evaluation;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    @Query(
        value = "SELECT * FROM evaluation " + 
        "WHERE evaluation.student_id = ?1 AND " +
        "evaluation.type = ?2 AND " +
        "evaluation.subject = ?3 AND " +
        "evaluation.trimester = ?4",
        nativeQuery = true
    )
    Optional<Evaluation> findOutIfExists(Long studentId, String type, String subject, Long trimester);

    @Query(
        value = "SELECT * FROM evaluation " + 
        "WHERE evaluation.type = ?1 AND " +
        "evaluation.subject = ?2 AND " +
        "evaluation.trimester = ?3" ,
        nativeQuery = true
    )
    Optional<Evaluation> findByTypeTrimesterAndSubject(String type, String subject, Long trimester);

    @Query(
        value = "SELECT * FROM evaluation " + 
        "WHERE evaluation.student_id = ?1",
        nativeQuery = true
    )
    List<Evaluation> findByStudentId(Long id);

    @Query(
        value = "SELECT * FROM evaluation " + 
        "WHERE evaluation.student_id = ?1 AND " +
        "evaluation.trimester = ?2",
        nativeQuery = true
    )
    List<Evaluation> findByStudentIdAndTrimester(Long id, Long trimester);
}
