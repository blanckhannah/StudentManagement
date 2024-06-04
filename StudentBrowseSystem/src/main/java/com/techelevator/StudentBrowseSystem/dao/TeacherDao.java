package com.techelevator.StudentBrowseSystem.dao;

import com.techelevator.StudentBrowseSystem.models.Teacher;

import java.util.List;

public interface TeacherDao {
    public List<Teacher> getAllTeachers();
    public Teacher getTeacherById(int id);
    public List<Teacher> getTeacherByLastName(String lastName);
    public Teacher createTeacher(Teacher teacher);
    public Teacher updateTeacher(Teacher teacher);
    public int deleteTeacherById(int id);
}
