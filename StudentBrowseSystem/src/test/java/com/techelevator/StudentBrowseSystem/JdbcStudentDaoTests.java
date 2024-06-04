package com.techelevator.StudentBrowseSystem;

import com.techelevator.StudentBrowseSystem.controllers.StudentController;
import com.techelevator.StudentBrowseSystem.dao.JdbcStudentDAO;
import com.techelevator.StudentBrowseSystem.dao.StudentDao;
import com.techelevator.StudentBrowseSystem.models.Student;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

public class JdbcStudentDaoTests extends StudentBrowseSystemApplicationTests{

    private static final Student STUDENT_1 = new Student(1, "John", "Doe", "123 Main St", 3);
    private static final Student STUDENT_2 = new Student(2, "Jane", "Doe", "456 Main St", 4);
    private static final Student STUDENT_3 = new Student(3, "Bob", "Smith", "789 Main St", 1);

    private JdbcStudentDAO dao;
    private JdbcTemplate jdbcTemplate;



}
