package com.floriantoenjes.instateam.web.controller;

import com.floriantoenjes.instateam.model.Collaborator;
import com.floriantoenjes.instateam.model.Role;
import com.floriantoenjes.instateam.service.CollaboratorService;
import com.floriantoenjes.instateam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class CollaboratorController {
    @Autowired
    CollaboratorService collaboratorService;

    @Autowired
    RoleService roleService;

    @RequestMapping("/collaborators")
    public String listCollaborators(Model model) {
        List<Collaborator> collaborators = collaboratorService.findAll();
        List<Role> roles = roleService.findAll();

        model.addAttribute("collaborator", new Collaborator());
        model.addAttribute("collaborators", collaborators);
        model.addAttribute("roles", roles);

        return "collaborators";
    }

    @RequestMapping(value = "/collaborators", method = RequestMethod.POST)
    public String addCollaborator(Collaborator collaborator) {
        collaboratorService.save(collaborator);

        return "redirect:/collaborators";
    }
}
