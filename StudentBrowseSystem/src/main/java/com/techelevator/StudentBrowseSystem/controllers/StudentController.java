package com.techelevator.StudentBrowseSystem.controllers;

import com.techelevator.StudentBrowseSystem.dao.StudentDao;
import com.techelevator.StudentBrowseSystem.models.Student;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentDao studentDao;
    public StudentController(StudentDao studentDao) {
        this.studentDao = studentDao;
    }
    @GetMapping
    public List<Student> getAllStudents() {
        return studentDao.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable int id) {
        Student student = studentDao.getStudentById(id);
        if(student == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        } else {
            return student;
        }
    }
    @GetMapping("/byLastName")
    public List<Student> getStudentByLastName(@RequestParam (defaultValue = "") String lastName) {
   // public List<Student> getStudentByLastName(@PathVariable String lastName) {
        List<Student> students = studentDao.getStudentsByLastName(lastName);
        if (students == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
        } else {
            return students;
        }
    }

    @GetMapping("/byTeacher")
    public List<Student> getStudentsByTeacher(@RequestParam (defaultValue = "") String teacher) {
        List<Student> students = studentDao.getStudentsByTeacherName(teacher);
        if (students == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Students not found");
        } else {
            return students;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Student createStudent(@RequestBody Student student){
        return studentDao.createStudent(student);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Student updateStudent(@RequestBody Student student, @PathVariable int id) {
        student.setId(id);
        return studentDao.updateStudent(student);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable int id) {
        int rowsAffected = studentDao.deleteStudentById(id);
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found", null);
        }
    }
}
