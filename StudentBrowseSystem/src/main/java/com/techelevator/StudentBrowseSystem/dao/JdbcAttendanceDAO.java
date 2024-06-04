package com.techelevator.StudentBrowseSystem.dao;

import com.techelevator.StudentBrowseSystem.exception.DaoException;
import com.techelevator.StudentBrowseSystem.models.Attendance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcAttendanceDAO implements AttendanceDao {
    private JdbcTemplate jdbcTemplate;
    public JdbcAttendanceDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public List<Attendance> getAllAttendances() {
        List<Attendance> attendances = new ArrayList<>();

        String sql = "SELECT * FROM Attendance";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Attendance attendance = mapRowToAttendance(results);
            attendances.add(attendance);
        }
        return attendances;
    }

    @Override
    public Attendance getAttendanceById(int id) {
        Attendance attendance = null;
        String sql = "SELECT * FROM attendance WHERE attendance_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if(results.next()) {
                attendance = mapRowToAttendance(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return attendance;
    }

    @Override
    public List<Attendance> getAttendanceByStudentId(int id) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE student_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while(results.next()) {
                Attendance attendance = mapRowToAttendance(results);
                attendances.add(attendance);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return attendances;
    }

    @Override
    public List<Attendance> getAttendanceByStudentName(String lastName, String firstName) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM attendance JOIN student ON student.student_id = attendance.student_id WHERE last_name = ? AND first_name = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, lastName, firstName);
            while (results.next()) {
                Attendance attendanceResult = mapRowToAttendance(results);
                attendances.add(attendanceResult);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return attendances;
    }

    @Override
    public Attendance createAttendance(Attendance attendance) {
        Attendance newAttendance = null;
        String sql = "INSERT INTO attendance (attendance_date, present, tardy, student_id) " +
                                "VALUES (?, ?, ?, ?) RETURNING attendance_id;";
        try {
            int newAttendanceId = jdbcTemplate.queryForObject(sql, int.class, attendance.getAttendanceDate(),
                    attendance.isPresent(), attendance.isTardy(), attendance.getStudent_id());
            newAttendance = getAttendanceById(newAttendanceId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newAttendance;
    }

    @Override
    public Attendance updateAttendance(Attendance attendance) {
        Attendance updatedAttendance = null;
        String sql = "UPDATE attendance SET attendance_date = ?, present = ?, tardy = ?, student_id = ? " +
                "WHERE attendance_id = ?;";
        try {
            int numberOfRows = jdbcTemplate.update(sql, attendance.getAttendanceDate(), attendance.isPresent(),
                    attendance.isTardy(), attendance.getStudent_id(), attendance.getId());
            if (numberOfRows == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                updatedAttendance = getAttendanceById(attendance.getId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedAttendance;    }

    @Override
    public int deleteAttendanceById(int id) {
        int numberOfRows = 0;
        String deleteAttendanceSql = "DELETE FROM attendance WHERE attendance_id = ?;";
        try {
            numberOfRows = jdbcTemplate.update(deleteAttendanceSql, id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numberOfRows;    }

    public Attendance mapRowToAttendance (SqlRowSet result) {
        Attendance attendance = new Attendance();
        attendance.setId(result.getInt("attendance_id"));
        attendance.setStudent_id(result.getInt("student_id"));
        Date attendanceDate = result.getDate("attendance_date");
        attendance.setAttendanceDate(attendanceDate != null ? attendanceDate.toLocalDate() : null);
        attendance.setPresent(result.getBoolean("present"));
        attendance.setTardy(result.getBoolean("tardy"));
        return attendance;
    }
}
