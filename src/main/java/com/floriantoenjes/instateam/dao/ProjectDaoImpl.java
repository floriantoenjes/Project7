package com.floriantoenjes.instateam.dao;

import com.floriantoenjes.instateam.model.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectDaoImpl implements ProjectDao {
    @Autowired
    SessionFactory sessionFactory;

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
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(project);
        session.getTransaction().commit();
        session.close();
    }
}
