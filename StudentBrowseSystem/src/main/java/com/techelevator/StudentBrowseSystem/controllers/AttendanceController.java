package com.techelevator.StudentBrowseSystem.controllers;

import com.techelevator.StudentBrowseSystem.dao.AttendanceDao;
import com.techelevator.StudentBrowseSystem.models.Attendance;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    private final AttendanceDao attendanceDao;

    public AttendanceController(AttendanceDao attendanceDao) {
        this.attendanceDao = attendanceDao;
    }

    @GetMapping
    public List<Attendance> getAllAttendances() {
        return attendanceDao.getAllAttendances();
    }

    @GetMapping("/{id}")
    public Attendance getAttendanceById(@PathVariable int id) {
        Attendance attendance = attendanceDao.getAttendanceById(id);
        if (attendance == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not found");
        } else {
            return attendance;
        }
    }

    @GetMapping("/byStudentId")
    public List<Attendance> getAttendanceByStudentID(@RequestParam(defaultValue = "") int id) {
        List<Attendance> attendances = attendanceDao.getAttendanceByStudentId(id);
        if (attendances == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance for student not found");
        } else {
            return attendances;
        }
    }


    @GetMapping("/byStudentName")
    public List<Attendance> getAttendanceByStudentName(@RequestParam(defaultValue = "") String lastName, String firstName) {
        List<Attendance> attendances = attendanceDao.getAttendanceByStudentName(lastName, firstName);
        if (attendances == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance Record not found");
        } else {
            return attendances;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Attendance createAttendance(@RequestBody Attendance attendance) {
        return attendanceDao.createAttendance(attendance);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Attendance updateAttendance(@RequestBody Attendance attendance, @PathVariable int id) {
        attendance.setId(id);
        return attendanceDao.updateAttendance(attendance);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    @DeleteMapping ("/{id}")
    public void deleteAttendance(@PathVariable int id) {
        int rowsAffected = attendanceDao.deleteAttendanceById(id);
        if (rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not found", null);
        }
    }
}
