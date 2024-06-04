package com.techelevator.StudentBrowseSystem.dao;

import com.techelevator.StudentBrowseSystem.models.Parent;

import java.util.List;

public interface ParentDao {
    List<Parent> getAllParents();
    Parent getParentById(int id);
    List<Parent> getParentsByLastName(String lastName);
    Parent createParent(Parent parent);
    Parent updateParent(Parent parent);
    int deleteParentById(int id);
}
