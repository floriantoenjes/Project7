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
        return null;
    }

    @Override
    public Collaborator findById(int id) {
        return null;
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
