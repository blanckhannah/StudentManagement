package com.techelevator.StudentBrowseSystem.controllers;

import com.techelevator.StudentBrowseSystem.dao.DisciplineDao;
import com.techelevator.StudentBrowseSystem.models.Discipline;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/discipline")
public class DisciplineController {
    private final DisciplineDao disciplineDao;
    public DisciplineController(DisciplineDao disciplineDao) {
        this.disciplineDao = disciplineDao;
    }

    @GetMapping
    public List<Discipline> getAllDiscipline() {
        return disciplineDao.getAllDisciplines();
    }


    @GetMapping("/{id}")
    public Discipline getDisciplineById(@PathVariable int id) {
        Discipline discipline = disciplineDao.getDisciplineById(id);
        if(discipline == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found");
        } else {
            return discipline;
        }
    }

    @GetMapping("/byStudentId")
    public List<Discipline> getDisciplineByStudentId(@RequestParam(defaultValue = "") int id) {
        List<Discipline> disciplines = disciplineDao.getDisciplineByStudentId(id);
        if (disciplines == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline for student not found");
        } else {
            return disciplines;
        }
    }


    @GetMapping("/byStudentName")
    public List<Discipline> getDisciplineByStudentName(@RequestParam(defaultValue = "") String lastName, String firstName) {
        List<Discipline> disciplines = disciplineDao.getDisciplineByStudentName(lastName, firstName);
        if (disciplines == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline for student not found");
        } else {
            return disciplines;
        }
    }


    @PostMapping
    public Discipline createDiscipline(@RequestBody Discipline discipline) {
        return disciplineDao.createDiscipline(discipline);
    }

    @PreAuthorize("hasRole('ADMIN)")
    @PutMapping("/{id}")
    public Discipline updateDiscipline(@RequestBody Discipline discipline, @PathVariable int id) {
        discipline.setId(id);
        return disciplineDao.updateDiscipline(discipline);
    }

    @PreAuthorize("hasRole('ADMIN)")
    @ResponseStatus (HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteDiscipline(@PathVariable int id) {
        int rowsAffected = disciplineDao.deleteDisciplineById(id);
        if(rowsAffected == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Discipline not found", null);
        }
    }
}
