package com.floriantoenjes.instateam.service;

import com.floriantoenjes.instateam.dao.RoleDao;
import com.floriantoenjes.instateam.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDao roleDao;

    @Override
    public List<Role> findAll() {
        List<Role> roles = roleDao.findAll();
        Collections.sort(roles, (role1, role2) -> role1.getName().compareTo(role2.getName()));
        return roles;
    }

    @Override
    public Role findById(int id) {
        return roleDao.findById(id);
    }

    @Override
    public void save(Role role) {
        roleDao.save(role);
    }
}