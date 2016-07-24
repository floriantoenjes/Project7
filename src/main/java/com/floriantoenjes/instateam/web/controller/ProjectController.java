package com.floriantoenjes.instateam.web.controller;

import com.floriantoenjes.instateam.model.Project;
import com.floriantoenjes.instateam.model.Role;
import com.floriantoenjes.instateam.service.ProjectService;
import com.floriantoenjes.instateam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Autowired
    RoleService roleService;

    @RequestMapping("/index")
    public String listProjects(Model model) {
        List<Project> projects = projectService.findAll();

        model.addAttribute("projects", projects);

        return "index";
    }

    @RequestMapping("/add")
    public String newProjectForm(Model model) {
        List<Role> roles =  roleService.findAll();

        model.addAttribute("project", new Project());
        model.addAttribute("roles", roles);

        return "edit_project";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addProject(Project project) {
        projectService.save(project);
        return "redirect:/index";
    }

}
