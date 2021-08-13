package br.com.webschool.api.assembler;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import br.com.webschool.api.model.StudentModel;
import br.com.webschool.api.model.input.StudentInput;
import br.com.webschool.domain.model.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Component
public class StudentAssembler {
    private ModelMapper modelMapper;

    public StudentModel toModel(Student student){
        student.getClassroom().setTeachers(null);
        student.getClassroom().setStudents(null);
        
        StudentModel res = modelMapper.map(student, StudentModel.class);

        return res;
    }

    public List<StudentModel> toCollectionModel(List<Student> students){

        students.forEach(student -> {
            student.getClassroom().setStudents(null);
            student.getClassroom().setTeachers(null);
        });

        List<StudentModel> res = students.stream()
                .map(this::toModel)
                .toList();

        return res;
    }

    @Getter
    @Setter
    public class ImprovedStudentPage<T>{
        
        public ImprovedStudentPage(
                List<T> studentsList, org.springframework.data.domain.Pageable pageable2,
                boolean last, long totalElements, int totalPages, int size, int number, boolean first,
                int numberOfElements, boolean empty
                ){

            this.content = studentsList;
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

    public ImprovedStudentPage<StudentModel> toPageModel(Page<Student> students){
        students.getContent().forEach( student -> {
            student.getClassroom().setStudents(null);
            student.getClassroom().setTeachers(null);
        });

        Page<Student> studentsCopy = students;

        List<StudentModel> studentsList = studentsCopy.getContent().stream()
        .map(this::toModel)
        .toList();

        ImprovedStudentPage<StudentModel> studentsPage = new ImprovedStudentPage<StudentModel>(studentsList, students.getPageable(), students.isLast(), students.getTotalElements(), students.getTotalPages(), students.getSize(), students.getNumber(), students.isFirst(), students.getNumberOfElements(), students.isEmpty());

        return studentsPage;
    }

    public Student toEntity(StudentInput studentInput){
        return modelMapper.map(studentInput, Student.class);
    }
}
