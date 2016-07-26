package com.floriantoenjes.instateam.dao;

import com.floriantoenjes.instateam.model.Collaborator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CollaboratorDaoImpl implements CollaboratorDao {
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public List<Collaborator> findAll() {
        Session session = sessionFactory.openSession();
        List<Collaborator> collaborators = session.createCriteria(Collaborator.class).list();
        session.close();
        return collaborators;
    }

    @Override
    public Collaborator findById(int id) {
        Session session = sessionFactory.openSession();
        Collaborator collaborator = session.get(Collaborator.class, id);
        session.close();
        return collaborator;
    }

    @Override
    public void save(Collaborator collaborator) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(collaborator);
        session.getTransaction().commit();
        session.close();
    }
}
