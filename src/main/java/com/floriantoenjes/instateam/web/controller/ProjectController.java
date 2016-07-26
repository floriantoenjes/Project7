package com.floriantoenjes.instateam.web.controller;

import com.floriantoenjes.instateam.model.Collaborator;
import com.floriantoenjes.instateam.model.Project;
import com.floriantoenjes.instateam.model.Role;
import com.floriantoenjes.instateam.service.CollaboratorService;
import com.floriantoenjes.instateam.service.ProjectService;
import com.floriantoenjes.instateam.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @Autowired
    RoleService roleService;

    @Autowired
    CollaboratorService collaboratorService;

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
    public String projectDetail(@PathVariable Integer id, Model model) {
        Project project = projectService.findById(id);
        model.addAttribute("project", project);
        return "project_detail";
    }

    @RequestMapping("/project/{id}/collaborators")
    public String projectCollaborators(@PathVariable Integer id, Model model) {
        Project project = projectService.findById(id);
        List<Collaborator> collaborators = collaboratorService.findAll();
        model.addAttribute("project", project);
        model.addAttribute("collaborators", collaborators);
        return "project_collaborators";
    }

    @RequestMapping(value = "/project/{id}/collaborators", method = RequestMethod.POST)
    public String assignCollaborators(@RequestParam Map<String, String> params, @PathVariable Integer id) {
        Project project = projectService.findById(id);
//        params.forEach( (k, v) -> System.out.printf("%nKey: %s | Value: %s%n%n", k, v));
        params.forEach( (key, value) -> {
            int collaboratorId = Integer.parseInt(value);
            Collaborator collaborator = collaboratorService.findById(collaboratorId);
            project.getCollaborators().add(collaborator);
            System.out.println("Added " + collaborator.getName() + " to project " + project.getName());
        });
        return String.format("redirect:/project/%s", id);
    }

    private List<Role> parseRoles(String roles) {
        String[] roleIds = roles.split(",");
        List<Role> rolesNeeded = new ArrayList<>();
        Arrays.stream(roleIds).forEach( (roleId) -> {
            int id = Integer.parseInt(roleId);
            Role role = roleService.findById(id);
            rolesNeeded.add(role);
        });
        return rolesNeeded;
    }

}
