package com.floriantoenjes.instateam.web.controller;

import com.floriantoenjes.instateam.model.Project;
import com.floriantoenjes.instateam.model.Role;
import com.floriantoenjes.instateam.service.ProjectService;
import com.floriantoenjes.instateam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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
    public String addProject(@RequestParam("project_roles") String roles, Project project, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println("Errors:");
            result.getAllErrors().forEach(System.out::println);
            System.out.println();
            return "redirect:/add";
        }
        project.setRolesNeeded(parseRoles(roles));
        projectService.save(project);
        return "redirect:/index";
    }

    @RequestMapping("/project/{id}")
    public String projectDetail() {
        return "project_detail";
    }

    private List<Role> parseRoles(String roles) {
        List<Role> rolesNeeded = new ArrayList<>();
        String[] roleIds = roles.split(",");
        for (String roleId : roleIds) {
            int id = Integer.parseInt(roleId);
            Role role = roleService.findById(id);
            rolesNeeded.add(role);
        }
        return rolesNeeded;
    }

}
