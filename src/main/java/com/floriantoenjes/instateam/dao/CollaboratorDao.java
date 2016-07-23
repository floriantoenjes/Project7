package com.floriantoenjes.instateam.dao;

import com.floriantoenjes.instateam.model.Collaborator;

import java.util.List;

public interface CollaboratorDao {
    List<Collaborator> findAll();
    Collaborator findById(int id);
}
