package com.floriantoenjes.instateam.service;

import com.floriantoenjes.instateam.dao.ProjectDao;
import com.floriantoenjes.instateam.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    ProjectDao projectDao;

    @Override
    public List<Project> findAll() {
        List<Project> projects = projectDao.findAll();
        Collections.sort(projects, (p1, p2) -> -p1.getStartDate().compareTo(p2.getStartDate()));
        return projects;
    }

    @Override
    public Project findById(int id) {
        return projectDao.findById(id);
    }

    @Override
    public void save(Project project) {
        projectDao.save(project);
    }
}
