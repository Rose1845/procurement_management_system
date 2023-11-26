package com.rose.procurement.test;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentServiceImpl(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    @Override
    public StudentDto addStudent(StudentDto studentDto) {
        Student student = new Student();
        mapDtoToEntity(studentDto, student);
        Student savedStudent = studentRepository.save(student);
        return mapEntityToDto(savedStudent);
    }
    @Override
    public List<StudentDto> getAllStudents() {
        List<StudentDto> studentDtos = new ArrayList<>();
        List<Student> students = studentRepository.findAll();
        students.stream().forEach(student -> {
            StudentDto studentDto = mapEntityToDto(student);
            studentDtos.add(studentDto);
        });
        return studentDtos;
    }
    @Transactional
    @Override
    public StudentDto updateStudent(Integer id, StudentDto studentDto) {
        Student std = studentRepository.getOne(id);
        std.getCourses().clear();
        mapDtoToEntity(studentDto, std);
        Student student = studentRepository.save(std);
        return mapEntityToDto(student);
    }
    @Override
    public String deleteStudent(Integer studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        //Remove the related courses from student entity.
        if(student.isPresent()) {
            student.get().removeCourses();
            studentRepository.deleteById(student.get().getId());
            return "Student with id: " + studentId + " deleted successfully!";
        }
        return null;
    }
    private void mapDtoToEntity(StudentDto studentDto, Student student) {
        student.setName(studentDto.getName());
        if (null == student.getCourses()) {
            student.setCourses(new HashSet<>());
        }
        studentDto.getCourses().stream().forEach(courseName -> {
            Course course = courseRepository.findByName(String.valueOf(courseName));
            if (null == course) {
                course = new Course();
                course.setStudents(new HashSet<>());
            }
            course.setName(String.valueOf(courseName));
            student.addCourse(course);
        });
    }
    private StudentDto mapEntityToDto(Student student) {
        StudentDto responseDto = new StudentDto();
        responseDto.setName(student.getName());
        responseDto.setId(student.getId());
        responseDto.setCourses(student.getCourses().stream().map(Course::getName).collect(Collectors.toSet()));
        return responseDto;
    }
}