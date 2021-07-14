package br.com.webschool.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.webschool.domain.model.Teacher;
import br.com.webschool.domain.repository.TeacherRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/teachers")
@AllArgsConstructor
public class TeacherController {
    private TeacherRepository teacherRepository;

    @GetMapping()
    public List<Teacher> getAllTeachers(){
        return teacherRepository.findAll();
    }

    @GetMapping("/{teacherId}")
    public ResponseEntity<Teacher> getOneTeacher(@PathVariable Long teacherId){
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        if(teacher.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(teacher.get());
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Teacher createTeacher(@RequestBody Teacher teacher){
        return teacherRepository.save(teacher);
    }

    @PutMapping("/{teacherId}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long teacherId, @RequestBody Teacher teacher){
        if(!teacherRepository.existsById(teacherId)){
            return ResponseEntity.notFound().build();
        }

        teacher.setId(teacherId);
        Teacher savedTeacher = teacherRepository.save(teacher);

        return ResponseEntity.ok(savedTeacher);
    }

    @DeleteMapping("/{teacherId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long teacherId){
        if(!teacherRepository.existsById(teacherId)){
            return ResponseEntity.notFound().build();
        }

        teacherRepository.deleteById(teacherId);

        return ResponseEntity.noContent().build();
    }
}
