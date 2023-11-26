package com.rose.procurement.test;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "STUDENT")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
    @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence")
    private Integer id;
    @Column(name = "name")
    private String name;
    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "STUDENT_COURSE", joinColumns = { @JoinColumn(name = "STUDENT_ID") }, inverseJoinColumns = {
            @JoinColumn(name = "COURSE_ID") })
    private Set<Course> courses;
    public void addCourse(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);
    }
    public void removeCourse(Course course) {
        this.getCourses().remove(course);
        course.getStudents().remove(this);
    }
    public void removeCourses() {
        for (Course course : new HashSet<>(courses)) {
            removeCourse(course);
        }
    }
}