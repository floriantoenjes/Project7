package com.floriantoenjes.instateam.service;

import com.floriantoenjes.instateam.dao.ProjectDao;
import com.floriantoenjes.instateam.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    ProjectDao projectDao;

    @Override
    public List<Project> findAll() {
        return null;
    }

    @Override
    public Project findById(int id) {
        return null;
    }

    @Override
    public void save(Project project) {
        projectDao.save(project);
    }
}
