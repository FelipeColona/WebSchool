package br.com.webschool.api.controller;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.webschool.domain.model.Evaluation;
import br.com.webschool.domain.model.Student;
import br.com.webschool.domain.repository.EvaluationRepository;
import br.com.webschool.domain.repository.StudentRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/students")
public class StudentController {
    StudentRepository studentRepository;
    EvaluationRepository evaluationRepository;

    @Controller
    public class PageRenderer {
        @GetMapping("/students/login")
        public String loginPage(){
            return "students-login";
        }

        @GetMapping("/students")
        public String home(Model model){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if(this.isStudent(principal)){
                Student currentStudent = ((Student)principal);
                List<Evaluation> evaluations = evaluationRepository.findByStudentId(currentStudent.getId());
                evaluations.forEach(evaluation -> {
                    evaluation.setStudent(null);
                });

                currentStudent.setEvaluations(evaluations);
                model.addAttribute("currentStudent", currentStudent);

                return "students-home";
            }else {
                return "redirect:" + "/403";
            }
        }

        public boolean isStudent(Object principal) {
            try{
                ((Student)principal).getName();
                return true;
            }catch (ClassCastException ex){
                return false;
            }
        }
    }

}
