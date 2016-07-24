package com.floriantoenjes.instateam.service;

import com.floriantoenjes.instateam.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();
    Role findById(int id);
    void save(Role role);
}
