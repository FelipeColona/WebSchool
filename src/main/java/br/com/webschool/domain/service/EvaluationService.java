package br.com.webschool.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.com.webschool.api.assembler.EvaluationAssembler;
import br.com.webschool.api.exceptionhandler.NotUniqueException;
import br.com.webschool.api.exceptionhandler.ResourceNotFoundException;
import br.com.webschool.api.exceptionhandler.ErrorDetails.Field;
import br.com.webschool.api.model.input.EvaluationInput;
import br.com.webschool.domain.model.Evaluation;
import br.com.webschool.domain.model.Student;
import br.com.webschool.domain.repository.EvaluationRepository;
import br.com.webschool.domain.repository.StudentRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class EvaluationService {
    StudentRepository studentRepository;
    EvaluationAssembler evaluationAssembler;
    EvaluationRepository evaluationRepository;
    
    @Transactional
    public Evaluation save(EvaluationInput evaluationInput){

        Optional<Student> existingStudent = studentRepository.findByName(evaluationInput.getStudent().getName());

        if(existingStudent.isEmpty()){
            List<Field> fields = new ArrayList<>();
            fields.add(new Field("student", "Student not found"));
            throw new ResourceNotFoundException(fields);
        }

        Optional<Evaluation> existingEvaluation = evaluationRepository.findOutIfExists(existingStudent.get().getId(), evaluationInput.getType(), evaluationInput.getSubject(), evaluationInput.getTrimester());
        if(existingEvaluation.isPresent()){
            List<Field> fields = new ArrayList<>();
            fields.add(new Field("type", "There must not be two evaluations with the same type in the same trimester"));
            throw new NotUniqueException(fields);
        }

        evaluationInput.getStudent().setId(existingStudent.get().getId());
        
        return evaluationRepository.save(evaluationAssembler.toEntity(evaluationInput));
    }

    @Transactional
    public void delete(Long evaluationId){
        evaluationRepository.deleteById(evaluationId);
    }

    @Transactional
    public Evaluation update(Long evaluationId, EvaluationInput evaluationInput){
        Evaluation evaluation = evaluationAssembler.toEntity(evaluationInput);

        Evaluation evaluationFound = evaluationRepository.findById(evaluationId).get();
        Optional<Evaluation> evaluationFound2 = evaluationRepository.findByTypeTrimesterAndSubject(evaluation.getType(), evaluation.getSubject(), evaluation.getTrimester());

        if(evaluationFound2.isPresent() && !evaluationFound.getId().equals(evaluationId) || !evaluationFound.equals(evaluationFound2.get())){
            List<Field> fields = new ArrayList<>();
            fields.add(new Field("type", "There must not be two evaluations with the same type in the same trimester"));
            throw new NotUniqueException(fields);
        }
        
        evaluation.setId(evaluationId);

        //For the consumer of the API not to send a different subject or student
        evaluation.setStudent(evaluationFound.getStudent());

        return evaluationRepository.save(evaluation);
    }
}
