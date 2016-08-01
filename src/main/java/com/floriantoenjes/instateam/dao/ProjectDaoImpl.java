package com.floriantoenjes.instateam.dao;

import com.floriantoenjes.instateam.model.Project;
import org.hibernate.Hibernate;
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
    @SuppressWarnings("unchecked")
    public List<Project> findAll() {
        Session session = sessionFactory.openSession();
        List<Project> projects = session.createCriteria(Project.class).list();
        session.close();
        return projects;
    }

    @Override
    public Project findById(int id) {
        Session session = sessionFactory.openSession();
        Project project = session.get(Project.class, id);
        Hibernate.initialize(project.getRolesNeeded());
        Hibernate.initialize(project.getCollaborators());
        session.close();
        return project;
    }

    @Override
    public void save(Project project) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(project);
        session.getTransaction().commit();
        session.close();
    }
}
