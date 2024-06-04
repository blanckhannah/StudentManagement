package com.techelevator.StudentBrowseSystem.dao;

import com.techelevator.StudentBrowseSystem.exception.DaoException;
import com.techelevator.StudentBrowseSystem.models.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcParentDAO implements ParentDao {
    private JdbcTemplate jdbcTemplate;
    public JdbcParentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public List<Parent> getAllParents() {
        List<Parent> parents = new ArrayList<>();

        String sql = "SELECT * FROM Parent";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                Parent parent = mapRowToParent(results);
                parents.add(parent);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return parents;
    }


    @Override
    public Parent getParentById(int id) {
        Parent parent = null;
        String sql = "SELECT * FROM parent WHERE parent_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                parent = mapRowToParent(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return parent;    }

    @Override
    public List<Parent> getParentsByLastName(String lastName) {
        List<Parent> parents = new ArrayList<>();
        String sql = "SELECT * FROM parent WHERE last_name ILIKE ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "%" + lastName + "%");
            while (results.next()) {
                Parent parentResult = mapRowToParent(results);
                parents.add(parentResult);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return parents;    }

    @Override
    public Parent createParent(Parent parent) {
        Parent newParent = null;

        String sql = "INSERT INTO parent (first_name, last_name, address, phone_number) " +
                "VALUES (?, ?, ?, ?) RETURNING parent_id;";
        try {
            int newParentId = jdbcTemplate.queryForObject(sql, int.class, parent.getFirstName(),
                    parent.getLastName(), parent.getAddress(), parent.getPhoneNumber());
            newParent = getParentById(newParentId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return  newParent;
    }

    @Override
    public Parent updateParent(Parent parent) {
        Parent updatedParent = null;

        String sql = "UPDATE parent SET first_name = ?, last_name = ?, address = ?, phone_number = ? " +
                "WHERE parent_id = ?;";
        try {
            int numberOfRows = jdbcTemplate.update(sql, parent.getFirstName(), parent.getLastName(),
                    parent.getAddress(), parent.getPhoneNumber(), parent.getId());
            if (numberOfRows == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                updatedParent = getParentById(parent.getId());
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedParent;    }

    @Override
    public int deleteParentById(int id) {
        int numberOfRows = 0;
        String deleteStudentParentSql = "DELETE FROM student_parent WHERE parent_id = ?;";
        String deleteParentSql = "DELETE FROM parent WHERE parent_id = ?;";
        try {
            jdbcTemplate.update(deleteStudentParentSql, id);
            numberOfRows = jdbcTemplate.update(deleteParentSql, id);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return numberOfRows;    }

    private Parent mapRowToParent(SqlRowSet result) {
        Parent parent = new Parent();
        parent.setId(result.getInt("parent_id"));
        parent.setFirstName(result.getString("first_name"));
        parent.setLastName(result.getString("last_name"));
        parent.setAddress(result.getString("address"));
        parent.setPhoneNumber(result.getString("phone_number"));
        return parent;
    }
}
