package com.floriantoenjes.instateam.web.controller;

import com.floriantoenjes.instateam.model.Collaborator;
import com.floriantoenjes.instateam.model.Role;
import com.floriantoenjes.instateam.service.CollaboratorService;
import com.floriantoenjes.instateam.service.RoleService;
import com.floriantoenjes.instateam.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // Todo: Add map for collaborator to role
        Map<Collaborator, Role> roleCollaborators = new HashMap<>();
        for (Collaborator collab : collaborators) {
            Role role = collab.getRole();
            if (role == null) {
                role = new Role();
                role.setName("Undefined");
            }
            roleCollaborators.put(collab, role);
        }

        model.addAttribute("collaborator", new Collaborator());
        model.addAttribute("roleCollaborators", roleCollaborators);
//        model.addAttribute("collaborators", collaborators);
        model.addAttribute("roles", roles);


        return "collaborators";
    }

    @RequestMapping(value = "/collaborators", method = RequestMethod.POST)
    public String addCollaborator(@Valid Collaborator collaborator, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(System.out::println);
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Failed to add collaborator", FlashMessage.Status.SUCCESS));
        } else {
            collaboratorService.save(collaborator);
        }

        return "redirect:/collaborators";
    }

    @RequestMapping("/collaborators/{id}")
    public String editCollaboratorForm(@PathVariable Integer id, Model model) {
        Collaborator collaborator = collaboratorService.findById(id);
        List<Role> roles = roleService.findAll();
        model.addAttribute("collaborator", collaborator);
        model.addAttribute("roles", roles);
        return "collaborator_detail";
    }
}
