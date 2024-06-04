package com.techelevator.StudentBrowseSystem.controllers;

import com.techelevator.StudentBrowseSystem.dao.TeacherDao;
import com.techelevator.StudentBrowseSystem.models.Teacher;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/teachers")
public class TeacherController {
    private TeacherDao teacherDao;

    public TeacherController(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }
    @PreAuthorize("permitAll()")
    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherDao.getAllTeachers();
    }

    @GetMapping("/{id}")
    public Teacher getTeacherById(@PathVariable int id) {
        Teacher teacher = teacherDao.getTeacherById(id);
        if(teacher == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found");
        } else {
            return teacher;
        }
    }
    @PreAuthorize("permitAll()")
    @GetMapping("/byLastName")
    public List<Teacher> getTeachersByLastName(@RequestParam(defaultValue = "") String lastName) {
        List<Teacher> teachers = teacherDao.getTeacherByLastName(lastName);
        if(teachers == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found");
        } else {
            return teachers;
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Teacher createTeacher(@RequestBody Teacher teacher) {
        return teacherDao.createTeacher(teacher);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Teacher updateTeacher(@RequestBody Teacher teacher, @PathVariable int id) {
        teacher.setId(id);
        return teacherDao.updateTeacher(teacher);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable int id) {
        int rowsAffected = teacherDao.deleteTeacherById(id);
        if(rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found", null);
        }
    }
}
