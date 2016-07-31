package com.floriantoenjes.instateam.web.controller;

import com.floriantoenjes.instateam.model.Role;
import com.floriantoenjes.instateam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RoleController {
    @Autowired
    RoleService roleService;

    @RequestMapping("/roles")
    public String listRoles(Model model) {
        List<Role> roles = roleService.findAll();
        model.addAttribute("role", new Role());
        model.addAttribute("roles", roles);
        return "roles";
    }

    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public String addRole(@Valid Role role, BindingResult result) {
        if (!result.hasErrors()) {
            roleService.save(role);
        }

        return "redirect:/roles";
    }
}
