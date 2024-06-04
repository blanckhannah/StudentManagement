package com.techelevator.StudentBrowseSystem.controllers;

import com.techelevator.StudentBrowseSystem.dao.ParentDao;
import com.techelevator.StudentBrowseSystem.models.Parent;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/parents")
public class ParentController {
    private final ParentDao parentDao;
    public ParentController(ParentDao parentDao) {
        this.parentDao = parentDao;
    }

    @GetMapping
    public List<Parent> getAllParents(){
        return parentDao.getAllParents();
    }

    @GetMapping("/{id}")
    public Parent getParentById(@PathVariable int id) {
        Parent parent = parentDao.getParentById(id);
        if(parent == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent not found");
        } else {
            return parent;
        }
    }

    @GetMapping("/byLastName")
    public List<Parent> getParentByLastName(@RequestParam (defaultValue = "") String lastName) {
        List<Parent> parents = parentDao.getParentsByLastName(lastName);
        if (parents == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent not found");
        } else {
            return parents;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Parent createParent(@RequestBody Parent parent) {
        return parentDao.createParent(parent);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Parent updateParent(@RequestBody Parent parent, @PathVariable int id){
        parent.setId(id);
        return parentDao.updateParent(parent);
    }

   @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteParent(@PathVariable int id) {
        int rowsAffected = parentDao.deleteParentById(id);
        if(rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parent not found", null);
        }
    }
}
