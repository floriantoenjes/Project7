package com.floriantoenjes.instateam.service;

import com.floriantoenjes.instateam.dao.CollaboratorDao;
import com.floriantoenjes.instateam.model.Collaborator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CollaboratorServiceImpl implements CollaboratorService {
    @Autowired
    CollaboratorDao collaboratorDao;

    @Override
    public List<Collaborator> findAll() {
        List<Collaborator> collaborators = collaboratorDao.findAll();
        Collections.sort(collaborators, (col1, col2) -> col1.getName().compareTo(col2.getName()));
        return collaborators;
    }

    @Override
    public Collaborator findById(int id) {
        return collaboratorDao.findById(id);
    }

    @Override
    public void save(Collaborator collaborator) {
        collaboratorDao.save(collaborator);
    }
}
