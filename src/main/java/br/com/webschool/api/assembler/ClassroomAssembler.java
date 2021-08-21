package br.com.webschool.api.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import br.com.webschool.api.model.ClassroomModel;
import br.com.webschool.api.model.input.ClassroomInput;
import br.com.webschool.domain.model.Classroom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Component
public class ClassroomAssembler {
    private ModelMapper modelMapper;

    public ClassroomModel toModel(Classroom classroom){
        classroom.getTeachers().forEach(teacher -> {
            teacher.setClassrooms(null);
        });

        classroom.getStudents().forEach(student -> {
            student.setClassroom(null);
        });
        
        ClassroomModel res = modelMapper.map(classroom, ClassroomModel.class);

        return res;
    }

    public List<ClassroomModel> toCollectionModel(List<Classroom> classrooms){

        classrooms.forEach(classroom -> {
            classroom.getTeachers().forEach(teacher -> {
                teacher.setClassrooms(null);
            });
        });

        List<ClassroomModel> res = classrooms.stream()
                .map(this::toModel)
                .toList();

        return res;
    }

    @Getter
    @Setter
    public class ImprovedClassroomPage<T>{
        
        public ImprovedClassroomPage(
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

    public ImprovedClassroomPage<ClassroomModel> toPageModel(Page<Classroom> classrooms){

        classrooms.getContent().forEach( classroom -> {
            classroom.getTeachers().forEach( teacher -> {
                teacher.setClassrooms(null);
            });
        });

        classrooms.getContent().forEach( classroom -> {
            classroom.getStudents().forEach( student -> {
                student.setClassroom(null);
            });
        });

        Page<Classroom> classroomCopy = classrooms;

        List<ClassroomModel> classroomsList = classroomCopy.getContent().stream()
        .map(this::toModel)
        .toList();

        ImprovedClassroomPage<ClassroomModel> classroomsPage = new ImprovedClassroomPage<ClassroomModel>(classroomsList, classrooms.getPageable(), classrooms.isLast(), classrooms.getTotalElements(), classrooms.getTotalPages(), classrooms.getSize(), classrooms.getNumber(), classrooms.isFirst(), classrooms.getNumberOfElements(), classrooms.isEmpty());

        return classroomsPage;
    }

    public Classroom toEntity(ClassroomInput classroomInput){
        return modelMapper.map(classroomInput, Classroom.class);
    }
}
