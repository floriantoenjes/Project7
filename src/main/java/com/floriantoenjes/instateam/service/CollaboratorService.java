package com.floriantoenjes.instateam.service;

import com.floriantoenjes.instateam.model.Collaborator;

import java.util.List;

public interface CollaboratorService {
    List<Collaborator> findAll();
    Collaborator findById(int id);

    void save(Collaborator collaborator);
}
