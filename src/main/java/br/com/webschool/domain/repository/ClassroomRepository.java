package br.com.webschool.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.webschool.domain.model.Classroom;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long>{
    Optional<Classroom> findByName(String name);

    @Query(
        value = "SELECT * FROM classroom WHERE name LIKE %?1%", 
        nativeQuery = true
    )
    List<Classroom> findByPartialMatching(String name);

    @Modifying
    @Query(
        value = "DELETE FROM teacher_classroom WHERE classroom_id = ?1", 
        nativeQuery = true
    )
    int removeTeacherRelationshipByClassroomId(Long id);

    @Modifying
    @Query(
        value = "UPDATE student " +
        "SET student.classroom_id = NULL " + 
        "WHERE student.classroom_id = ?1",
        nativeQuery = true
    )
    int removeAllStudentRelationshipByClassroomId(Long id);

    @Modifying
    @Query(
        value = "UPDATE student " +
        "SET student.classroom_id = ?1 " + 
        "WHERE student.id = ?2",
        nativeQuery = true
    )
    void removeStudentRelationshipByClassroomId(Long classroomId, Long studentId);

    @Modifying
    @Query(
        value = "DELETE FROM classroom WHERE id = ?1", 
        nativeQuery = true
    )
    int deleteByClassroomId(Long id);
}
