package com.techelevator.StudentBrowseSystem.controllers;

import com.techelevator.StudentBrowseSystem.dao.UsersDao;
import com.techelevator.StudentBrowseSystem.models.User;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/users")
public class UsersController {
    private UsersDao usersDao;
    public UsersController(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return usersDao.getAllUsers();
    }

    @GetMapping("/{username}")
    public User getUserByUsername(String username) {
        return usersDao.getUserByUsername(username);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return usersDao.createUser(user);
    }

    @PutMapping("/{username}")
    public void updateUser(@PathVariable String username, @RequestBody User user) {
        user.setUsername(username);
        usersDao.updateUser(user);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
        usersDao.deleteUser(username);
    }

    @GetMapping("/{username}/roles")
    public List<String> getUserRoles(@PathVariable String username) {
        return usersDao.getUserRoles(username);
    }

    @PostMapping("/{username}/roles")
    public void addUserRole(@PathVariable String username, @RequestBody String role) {
        usersDao.addRoleToUser(username, role);
    }

    @DeleteMapping("/{username}/roles/{role}")
    public void deleteUserRole(@PathVariable String username, @PathVariable String role) {
        usersDao.removeRoleFromUser(username, role);
    }
}
