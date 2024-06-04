package com.techelevator.StudentBrowseSystem.dao;

import com.techelevator.StudentBrowseSystem.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class UsersDao {
    private JdbcTemplate jdbcTemplate;
    private PasswordEncoder passwordEncoder;
    public UsersDao(DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        SqlRowSet rows = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        List<User> users = new ArrayList<>();
        while (rows.next()) {
            users.add(mapRowToUser(rows));
        }
        return users;
    }
    public User getUserByUsername(String username) {
        SqlRowSet rows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE username = ?", username);
        if(rows.next()) {
            return mapRowToUser(rows);
        } else {
            return null;
        }
    }

    public User createUser(User user){
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        jdbcTemplate.update("INSERT INTO users (username, password) VALUES (?, ?)", user.getUsername(), hashedPassword);
        return user;
    }

    public void updateUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        jdbcTemplate.update("UPDATE users SET password = ? WHERE username = ?", hashedPassword, user.getUsername());
    }


    public void deleteUser(String username) {
        jdbcTemplate.update("DELETE FROM roles WHERE username = ?", username);
        jdbcTemplate.update("DELETE FROM users WHERE username = ?", username);
    }

    public List<String> getUserRoles(String username) {
        SqlRowSet rows = jdbcTemplate.queryForRowSet("SELECT rolename FROM roles WHERE username = ?", username);
        List<String> roles = new ArrayList<>();
        while (rows.next()) {
            roles.add(rows.getString("rolename"));
        }
        return roles;
    }

    public void addRoleToUser(String username, String role) {
        jdbcTemplate.update("INSERT INTO roles (username, rolename) VALUES (?, ?)", username, role);
    }

    public void removeRoleFromUser(String username, String role) {
        jdbcTemplate.update("DELETE FROM roles WHERE username = ? AND rolename = ?", username, role);
    }
    private User mapRowToUser(SqlRowSet row){
        User user = new User();
        user.setUsername(row.getString("username"));
        user.setPassword(row.getString("password"));
        return user;
    }
}
