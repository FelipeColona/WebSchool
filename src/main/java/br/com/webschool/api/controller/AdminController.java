package br.com.webschool.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.webschool.api.assembler.TeacherAssembler;
import br.com.webschool.api.model.TeacherModel;
import br.com.webschool.api.model.input.TeacherInput;
import br.com.webschool.domain.model.Teacher;
import br.com.webschool.domain.repository.TeacherRepository;
import br.com.webschool.domain.service.TeacherService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private TeacherService teacherService;
    private TeacherRepository teacherRepository;
    private TeacherAssembler teacherAssembler;

    @Controller
    public class PageRenderer {
        @GetMapping("/admin/login")
        public String loginPage(){
            return "admin-login";
        }
    }

    @GetMapping("/teachers")
    public List<TeacherModel> getAllTeachers(){
        List<Teacher> teachers = teacherRepository.findAll();

        return teacherAssembler.toCollectionModel(teachers);
    }

    @GetMapping("/teachers/{teacherId}")
    public ResponseEntity<TeacherModel> getOneTeacher(@PathVariable Long teacherId){
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        if(teacher.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(teacherAssembler.toModel(teacher.get()));
    }

    @PostMapping("/teachers")
    @ResponseStatus(HttpStatus.CREATED)
    public TeacherModel createTeacher(@RequestBody @Valid TeacherInput teacherInput){
        Teacher savedTeacher = teacherService.save(teacherInput);

        return teacherAssembler.toModel(savedTeacher);
    }

    @PutMapping("/teachers/{teacherId}")
    public ResponseEntity<TeacherModel> updateTeacher(@PathVariable Long teacherId, @RequestBody @Valid TeacherInput teacherInput){
        if(!teacherRepository.existsById(teacherId)){
            return ResponseEntity.notFound().build();
        }

        Teacher savedTeacher = teacherService.update(teacherId, teacherInput);

        return ResponseEntity.ok(teacherAssembler.toModel(savedTeacher));
    }

    @DeleteMapping("/teachers/{teacherId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long teacherId){
        if(!teacherRepository.existsById(teacherId)){
            return ResponseEntity.notFound().build();
        }

        teacherService.delete(teacherId);

        return ResponseEntity.noContent().build();
    }
}
