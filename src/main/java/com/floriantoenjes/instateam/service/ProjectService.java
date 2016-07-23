package com.floriantoenjes.instateam.service;

import com.floriantoenjes.instateam.model.Project;

import java.util.List;

public interface ProjectService {
    List<Project> findAll();
    Project findById(int id);
}
