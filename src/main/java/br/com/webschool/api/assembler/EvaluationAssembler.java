package br.com.webschool.api.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import br.com.webschool.api.model.EvaluationModel;
import br.com.webschool.api.model.input.EvaluationInput;
import br.com.webschool.domain.model.Evaluation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Component
@AllArgsConstructor
public class EvaluationAssembler {
    private ModelMapper modelMapper;

    public EvaluationModel toModel(Evaluation evaluation){
        if(evaluation.getStudent().getClassroom() != null){
            evaluation.getStudent().getClassroom().setStudents(null);
            evaluation.getStudent().getClassroom().setTeachers(null);
        }
        evaluation.getStudent().setEvaluations(null);

        EvaluationModel res = modelMapper.map(evaluation, EvaluationModel.class);

        return res;
    }

    public List<EvaluationModel> toCollectionModel(List<Evaluation> evaluations){
        List<EvaluationModel> res = evaluations.stream()
                .map(this::toModel)
                .toList();

        return res;
    }

    @Getter
    @Setter
    public class ImprovedEvaluationPage<T>{
        
        public ImprovedEvaluationPage(
                List<T> evaluationList, org.springframework.data.domain.Pageable pageable2,
                boolean last, long totalElements, int totalPages, int size, int number, boolean first,
                int numberOfElements, boolean empty
                ){

            this.content = evaluationList;
            this.pageable = null;
            this.last = last;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
            this.size = size;
            this.number = number;
            this.sort = null;
            this.first = first;
            this.numberOfElements = numberOfElements;
            this.empty = empty;
        }
        private List<T> content;
        private Pageable pageable; 
        private boolean last;
        private Long totalElements;
        private int totalPages;
        private int size;
        private int number;
        private Sort sort;
        private boolean first;
        private int numberOfElements;
        private boolean empty;

    }

    public ImprovedEvaluationPage<EvaluationModel> toPageModel(Page<Evaluation> evaluations){
        Page<Evaluation> evaluationsCopy = evaluations;

        List<EvaluationModel> evaluationsList = evaluationsCopy.getContent().stream()
        .map(this::toModel)
        .toList();

        ImprovedEvaluationPage<EvaluationModel> evaluationsPage = new ImprovedEvaluationPage<EvaluationModel>(evaluationsList, evaluations.getPageable(), evaluations.isLast(), evaluations.getTotalElements(), evaluations.getTotalPages(), evaluations.getSize(), evaluations.getNumber(), evaluations.isFirst(), evaluations.getNumberOfElements(), evaluations.isEmpty());

        return evaluationsPage;
    }

    public Evaluation toEntity(EvaluationInput evaluationInput){
        return modelMapper.map(evaluationInput, Evaluation.class);
    }
}
