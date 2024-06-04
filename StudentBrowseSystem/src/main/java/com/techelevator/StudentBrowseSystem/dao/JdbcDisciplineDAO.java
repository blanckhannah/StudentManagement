package com.techelevator.StudentBrowseSystem.dao;

import com.techelevator.StudentBrowseSystem.exception.DaoException;
import com.techelevator.StudentBrowseSystem.models.Discipline;
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
public class JdbcDisciplineDAO implements DisciplineDao {
    private JdbcTemplate jdbcTemplate;
    public JdbcDisciplineDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public List<Discipline> getAllDisciplines() {
        List<Discipline> disciplineList = new ArrayList<>();

        String sql = "SELECT * FROM discipline";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Discipline discipline = mapRowToDiscipline(results);
                disciplineList.add(discipline);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return disciplineList;
    }

    @Override
    public Discipline getDisciplineById(int id) {
        Discipline discipline = null;
        String sql = "SELECT * FROM discipline WHERE discipline_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                discipline = mapRowToDiscipline(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return discipline;    }

    @Override
    public List<Discipline> getDisciplineByStudentId(int id) {
        List<Discipline> referrals = new ArrayList<>();
        String sql = "SELECT * FROM discipline WHERE student_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                Discipline disciplineResult = mapRowToDiscipline(results);
                referrals.add(disciplineResult);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return referrals;    }

    @Override
    public List<Discipline> getDisciplineByStudentName(String lastName, String firstName) {
        List<Discipline> disciplines = new ArrayList<>();
        String sql = "SELECT * FROM discipline JOIN student ON student.student_id = discipline.student_id WHERE last_name = ? AND first_name = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, lastName, firstName);
            while (results.next()) {
                Discipline disciplineResult = mapRowToDiscipline(results);
                disciplines.add(disciplineResult);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return disciplines;
    }

    @Override
    public Discipline createDiscipline(Discipline discipline) {
        Discipline newDiscipline = null;
        String sql = "INSERT INTO discipline (student_id, referral_date, description, discipline_action, parent_contacted) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING discipline_id;";
        try{
            int newDisciplineId = jdbcTemplate.queryForObject(sql, int.class, discipline.getStudentId(), discipline.getReferralDate(),
                    discipline.getDescription(), discipline.getDisciplineAction(), discipline.isParentContacted());
            newDiscipline = getDisciplineById(newDisciplineId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newDiscipline;    }

    @Override
    public Discipline updateDiscipline(Discipline discipline) {
        Discipline updatedDiscipline = null;
        String sql = "UPDATE discipline SET student_id = ?, referral_date = ?, description = ?, discipline_action = ?, parent_contacted = ? " +
                "WHERE discipline_id = ?;";
        try {
            int numberOfRows = jdbcTemplate.update(sql, discipline.getStudentId(), discipline.getReferralDate(), discipline.getDescription(),
                    discipline.getDisciplineAction(), discipline.isParentContacted(), discipline.getId());
            if (numberOfRows == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                updatedDiscipline = getDisciplineById(discipline.getId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedDiscipline;    }

    @Override
    public int deleteDisciplineById(int id) {
        int numberOfRows = 0;
        String deleteDisciplineSql = "DELETE FROM discipline WHERE discipline_id = ?;";
        try {
            numberOfRows = jdbcTemplate.update(deleteDisciplineSql, id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numberOfRows;    }

    public Discipline mapRowToDiscipline (SqlRowSet result) {
        Discipline discipline = new Discipline();
        discipline.setId(result.getInt("discipline_id"));
        discipline.setStudentId(result.getInt("student_id"));
        Date referralDate = result.getDate("referral_date");
        discipline.setReferralDate(referralDate != null ? referralDate.toLocalDate() : null);
        discipline.setDescription(result.getString("description"));
        discipline.setDisciplineAction(result.getString("discipline_action"));
        discipline.setParentContacted(result.getBoolean("parent_contacted"));
        return discipline;
    }
}
