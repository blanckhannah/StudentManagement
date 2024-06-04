package com.techelevator.StudentBrowseSystem.dao;

import com.techelevator.StudentBrowseSystem.models.Student;

import java.util.List;

public interface StudentDao {

    /**
     * Get a student from the datastore that has the given id.
     * If the id is not found, return null.
     *
     * @param id the id of the student to get from the datastore
     * @return a Student object
     */
    Student getStudentById(int id);
    List<Student> getAllStudents();
    List<Student> getStudentsByLastName(String lastName);
    List<Student> getStudentsByTeacherName(String teacherName);
    Student createStudent(Student student);
    Student updateStudent(Student student);
    int deleteStudentById(int id);
}

