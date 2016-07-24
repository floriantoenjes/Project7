package com.floriantoenjes.instateam.dao;

import com.floriantoenjes.instateam.model.Role;

import java.util.List;

public interface RoleDao {
    List<Role> findAll();
    Role findById(int id);
    void save(Role role);
}
