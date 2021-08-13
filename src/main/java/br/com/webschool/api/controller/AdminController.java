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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.webschool.api.assembler.ClassroomAssembler;
import br.com.webschool.api.assembler.StudentAssembler;
import br.com.webschool.api.assembler.TeacherAssembler;
import br.com.webschool.api.assembler.ClassroomAssembler.ImprovedClassroomPage;
import br.com.webschool.api.assembler.TeacherAssembler.ImprovedPage;
import br.com.webschool.api.assembler.StudentAssembler.ImprovedStudentPage;
import br.com.webschool.api.common.UniqueChecker;
import br.com.webschool.api.model.ClassroomModel;
import br.com.webschool.api.model.StudentModel;
import br.com.webschool.api.model.TeacherModel;
import br.com.webschool.api.model.input.ClassroomInput;
import br.com.webschool.api.model.input.StudentInput;
import br.com.webschool.api.model.input.TeacherInput;
import br.com.webschool.domain.model.Classroom;
import br.com.webschool.domain.model.Student;
import br.com.webschool.domain.model.Teacher;
import br.com.webschool.domain.repository.ClassroomRepository;
import br.com.webschool.domain.repository.StudentRepository;
import br.com.webschool.domain.repository.TeacherRepository;
import br.com.webschool.domain.service.ClassroomService;
import br.com.webschool.domain.service.StudentService;
import br.com.webschool.domain.service.TeacherService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private TeacherService teacherService;
    private TeacherRepository teacherRepository;
    private TeacherAssembler teacherAssembler;

    private ClassroomRepository classroomRepository;
    private ClassroomAssembler classroomAssembler;
    private ClassroomService classroomService;

    private StudentRepository studentRepository;
    private StudentAssembler studentAssembler;
    private StudentService studentService;

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
        public String createTeacher(){
            return "admin-create-teacher";
        }

        @GetMapping("/admin/create-classroom")
        public String createClassroom(){
            return "admin-create-classroom";
        }

        @GetMapping("/admin/get-classrooms")
        public String getClassrooms(Model model, @PageableDefault(page = 0, size = 5, sort = "name", direction = Direction.ASC) Pageable pageable){
            Page<Classroom> allClassrooms = classroomRepository.findAll(pageable);
            model.addAttribute("allclassrooms", classroomAssembler.toPageModel(allClassrooms));
            
            return "admin-get-classrooms";
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
    public Teacher createTeacher(@RequestBody @Validated(UniqueChecker.class) TeacherInput teacherInput){
        Teacher savedTeacher = teacherService.save(teacherInput);

        return savedTeacher;
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



    @GetMapping("/classrooms")
    public ImprovedClassroomPage<ClassroomModel> getAllClassrooms(@PageableDefault(page = 0, size = 5, sort = "name", direction = Direction.ASC) Pageable pageable){
        Page<Classroom> classrooms = classroomRepository.findAll(pageable);

        return classroomAssembler.toPageModel(classrooms);
    }

    @GetMapping("/classrooms/{classroomId}")
    public ResponseEntity<ClassroomModel> getOneClassroom(@PathVariable Long classroomId){
        Optional<Classroom> classroom = classroomRepository.findById(classroomId);
        if(classroom.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(classroomAssembler.toModel(classroom.get()));
    }

    @GetMapping("/classroom/{classroomName}")
    public List<ClassroomModel> getClassroomByPartialName(@PathVariable String classroomName){
        List<Classroom> classrooms = classroomRepository.findByPartialMatching(classroomName);

        return classroomAssembler.toCollectionModel(classrooms);
    }


    @PostMapping("/classrooms")
    @ResponseStatus(HttpStatus.CREATED)
    public ClassroomModel createClassroom(@RequestBody @Validated(UniqueChecker.class) ClassroomInput classroomInput){
        Classroom savedClassroom = classroomService.save(classroomInput);

        return classroomAssembler.toModel(savedClassroom);
    }

    @DeleteMapping("/classrooms/{classroomId}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long classroomId){
        if(!classroomRepository.existsById(classroomId)){
            return ResponseEntity.notFound().build();
        }

        classroomService.delete(classroomId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/classrooms/{classroomId}")
    public ResponseEntity<ClassroomModel> updateClassroom(@PathVariable Long classroomId, @RequestBody @Valid ClassroomInput classroomInput){
        if(!classroomRepository.existsById(classroomId)){
            return ResponseEntity.notFound().build();
        }

        Classroom savedClassroom = classroomService.update(classroomId, classroomInput);

        return ResponseEntity.ok(classroomAssembler.toModel(savedClassroom));
    }

    @GetMapping("/students")
    public ImprovedStudentPage<StudentModel> getAllStudents(@PageableDefault(page = 0, size = 5, sort = "name", direction = Direction.ASC) Pageable pageable){
        Page<Student> students = studentRepository.findAll(pageable);

        return studentAssembler.toPageModel(students);
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<StudentModel> getOneStudent(@PathVariable Long studentId){
        Optional<Student> student = studentRepository.findById(studentId);
        if(student.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(studentAssembler.toModel(student.get()));
    }

    @GetMapping("/student/{studentName}")
    public List<StudentModel> getStudentByPartialName(@PathVariable String studentName){
        List<Student> students = studentRepository.findByPartialMatching(studentName);

        return studentAssembler.toCollectionModel(students);
    }

    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@RequestBody @Validated(UniqueChecker.class) StudentInput studentInput){
        Student savedStudent = studentService.save(studentInput);

        return savedStudent;
    }

    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId){
        if(!studentRepository.existsById(studentId)){
            return ResponseEntity.notFound().build();
        }

        studentService.delete(studentId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/students/{studentId}")
    public ResponseEntity<StudentModel> updateStudent(@PathVariable Long studentId, @RequestBody @Valid StudentInput classroomInput){
        if(!studentRepository.existsById(studentId)){
            return ResponseEntity.notFound().build();
        }

        Student savedStudent = studentService.update(studentId, classroomInput);

        return ResponseEntity.ok(studentAssembler.toModel(savedStudent));
    }
}
