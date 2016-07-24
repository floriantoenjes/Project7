package com.floriantoenjes.instateam.web.controller;

import com.floriantoenjes.instateam.model.Project;
import com.floriantoenjes.instateam.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ProjectController {
    @Autowired
    ProjectService projectService;

    @RequestMapping("/index")
    public String listProjects(Model model) {
        List<Project> projects = projectService.findAll();
        model.addAttribute("projects", projects);
        return "index";
    }

    @RequestMapping("/add")
    public String newProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "edit_project";
    }

}
