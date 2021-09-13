package br.com.webschool.api.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.servlet.view.RedirectView;

import br.com.webschool.api.assembler.EvaluationAssembler;
import br.com.webschool.api.common.UpdateEvaluation;
import br.com.webschool.api.model.input.EvaluationInput;
import br.com.webschool.domain.model.Evaluation;
import br.com.webschool.domain.model.Teacher;
import br.com.webschool.domain.repository.EvaluationRepository;
import br.com.webschool.domain.service.EvaluationService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("teachers/evaluations")
@AllArgsConstructor
public class EvaluationController {
    private EvaluationRepository evaluationRepository;
    private EvaluationAssembler evaluationAssembler;
    private EvaluationService evaluationService;

    @GetMapping
    public Object getAllEvaluations(@PageableDefault(page = 0, size = 5, sort = "trimester", direction = Direction.ASC) Pageable pageable){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (this.isTeacher(principal)) {
            Page<Evaluation> evaluationsPage = evaluationRepository.findAll(pageable);
            return evaluationAssembler.toPageModel(evaluationsPage);
        } else {
            return new RedirectView("/403");
        }
    }

    @GetMapping("/{evaluationId}")
    public Object getOneEvaluation(@PathVariable Long evaluationId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(this.isTeacher(principal)){

            Optional<Evaluation> evaluation = evaluationRepository.findById(evaluationId);
            if(evaluation.isEmpty()){
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok(evaluationAssembler.toModel(evaluation.get()));
            
        } else{
            return new RedirectView("/403");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Object createEvaluation(@RequestBody @Valid EvaluationInput evaluationInput){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (this.isTeacher(principal)) {
            Evaluation savedEvaluation = evaluationService.save(evaluationInput);
            return evaluationAssembler.toModel(savedEvaluation);
        } else {
            return new RedirectView("/403");
        }
    }

    @DeleteMapping("/{evaluationId}")
    public Object deleteEvaluation(@PathVariable Long evaluationId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (this.isTeacher(principal)) {
            if(!evaluationRepository.existsById(evaluationId)){
                return ResponseEntity.notFound().build();
            }
    
            evaluationService.delete(evaluationId);
    
            return ResponseEntity.noContent().build();
        } else {
            return new RedirectView("/403");
        }
    }

    @PutMapping("/{evaluationId}")
    public Object updateEvaluation(@PathVariable Long evaluationId, @RequestBody @Validated(UpdateEvaluation.class) EvaluationInput evaluationInput){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(this.isTeacher(principal)){    
            Evaluation savedEvaluation = evaluationService.update(evaluationId, evaluationInput);
    
            return ResponseEntity.ok(evaluationAssembler.toModel(savedEvaluation));
        } else {
            return new RedirectView("/403");
        }
    }

    public boolean isTeacher(Object principal) {
        try{
            ((Teacher) principal).getName();
            return true;
        }catch (ClassCastException ex){
            return false;
        }
    }
}
