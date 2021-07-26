package br.com.webschool.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import br.com.webschool.api.assembler.TeacherAssembler.ImprovedPage;
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

        @GetMapping("/admin")
        public String dashboard(){
            return "admin-dashboard";
        }

        @GetMapping("/admin/get-teachers")
        public String getTeachers(Model model, @PageableDefault(page = 0, size = 5, sort = "name", direction = Direction.ASC) Pageable pageable){
            Page<Teacher> allTeachers = teacherRepository.findAll(pageable);

            model.addAttribute("allteachers", teacherAssembler.toPageModel(allTeachers));
            
            return "admin-get-teachers";
        }

        @GetMapping("/admin/create-teacher")
        public String create(){
            return "admin-create-teacher";
        }
    }

    @GetMapping("/teachers")
    public ImprovedPage<TeacherModel> getAllTeachers(@PageableDefault(page = 0, size = 5, sort = "name", direction = Direction.ASC) Pageable pageable){
        Page<Teacher> teachers = teacherRepository.findAll(pageable);
        
        return teacherAssembler.toPageModel(teachers);
    }

    @GetMapping("/teachers/{teacherId}")
    public ResponseEntity<TeacherModel> getOneTeacher(@PathVariable Long teacherId){
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        if(teacher.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(teacherAssembler.toModel(teacher.get()));
    }

    @GetMapping("/teacher/{teacherName}")
    public List<TeacherModel> getTeacherByPartialName(@PathVariable String teacherName){
        List<Teacher> teachers = teacherRepository.findByPartialMatching(teacherName);

        return teacherAssembler.toCollectionModel(teachers);
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
