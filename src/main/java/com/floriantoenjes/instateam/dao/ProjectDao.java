package com.floriantoenjes.instateam.dao;

import com.floriantoenjes.instateam.model.Project;

import java.util.List;

public interface ProjectDao {
    List<Project> findAll();
    Project findById(int id);
}
