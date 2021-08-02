package br.com.webschool.api.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import br.com.webschool.api.model.TeacherModel;
import br.com.webschool.api.model.input.TeacherInput;
import br.com.webschool.domain.model.Teacher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Component
public class TeacherAssembler {
    private ModelMapper modelMapper;

    public TeacherModel toModel(Teacher teacher){
        teacher.getClassrooms().forEach(classroom -> {
            classroom.setTeachers(null);
        });

        return modelMapper.map(teacher, TeacherModel.class);
    }

    public List<TeacherModel> toCollectionModel(List<Teacher> teachers){
        return teachers.stream()
                .map(this::toModel)
                .toList();
    }

    @Getter
    @Setter
    public class ImprovedPage<T>{
        
        public ImprovedPage(
                List<T> teachersList, org.springframework.data.domain.Pageable pageable2,
                boolean last, long totalElements, int totalPages, int size, int number, boolean first,
                int numberOfElements, boolean empty
                ){

            this.content = teachersList;
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

    public ImprovedPage<TeacherModel> toPageModel(Page<Teacher> teachers){
        teachers.getContent().forEach( teacher -> {
            teacher.getClassrooms().forEach( classroom -> {
                classroom.setTeachers(null);
            });
        });

        Page<Teacher> teachersCopy = teachers;

        List<TeacherModel> teachersList = teachersCopy.getContent().stream()
        .map(this::toModel)
        .toList();

        ImprovedPage<TeacherModel> teachersPage = new ImprovedPage<TeacherModel>(teachersList, teachers.getPageable(), teachers.isLast(), teachers.getTotalElements(), teachers.getTotalPages(), teachers.getSize(), teachers.getNumber(), teachers.isFirst(), teachers.getNumberOfElements(), teachers.isEmpty());

        return teachersPage;
    }

    public Teacher toEntity(TeacherInput teacherInput){
        return modelMapper.map(teacherInput, Teacher.class);
    }
}
