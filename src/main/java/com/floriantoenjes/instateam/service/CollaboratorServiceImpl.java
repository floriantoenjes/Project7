package com.floriantoenjes.instateam.service;

import com.floriantoenjes.instateam.dao.CollaboratorDao;
import com.floriantoenjes.instateam.model.Collaborator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollaboratorServiceImpl implements CollaboratorService {
    @Autowired
    CollaboratorDao collaboratorDao;

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
        collaboratorDao.save(collaborator);
    }
}
