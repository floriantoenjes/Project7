package com.floriantoenjes.instateam.web.controller;

import com.floriantoenjes.instateam.model.Collaborator;
import com.floriantoenjes.instateam.model.Project;
import com.floriantoenjes.instateam.model.Role;
import com.floriantoenjes.instateam.service.CollaboratorService;
import com.floriantoenjes.instateam.service.ProjectService;
import com.floriantoenjes.instateam.service.RoleService;
import com.floriantoenjes.instateam.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

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

        if (!model.containsAttribute("project")) {
            model.addAttribute("project", new Project());
        }
        model.addAttribute("roles", roles);

        return "edit_project";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addProject(@Valid Project project, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.project", result);
            redirectAttributes.addFlashAttribute("project", project);

            return "redirect:/add";
        }

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Project has been created.",
                FlashMessage.Status.SUCCESS));
        project.setStartDate(LocalDateTime.now());
        projectService.save(project);

        return "redirect:/index";
    }

    @RequestMapping("/project/{id}")
    public String projectDetail(@PathVariable Integer id, Model model) {
        Project project = projectService.findById(id);

        Map<Role, Collaborator> roleColab = getRoleCollaboratorMap(project);

        model.addAttribute("project", project);
        model.addAttribute("rolecolab", roleColab);
        return "project_detail";
    }

    @RequestMapping("/project/{id}/edit")
    public String editProjectForm(@PathVariable Integer id, Model model) {
        if (!model.containsAttribute("project")) {
            Project project = projectService.findById(id);
            model.addAttribute("project", project);
        }

        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);

        return "edit_project";
    }

    @RequestMapping(value = "/project/{id}/edit", method = RequestMethod.POST)
    public String editProject(@PathVariable Integer id, @Valid Project project, BindingResult result,
                              RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.project", result);
            redirectAttributes.addFlashAttribute("project", project);

            return String.format("redirect:/project/%s/edit", id);
        }

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("The project has been updated.",
                FlashMessage.Status.SUCCESS));
        projectService.save(project);
        return String.format("redirect:/project/%s", id);
    }

    @RequestMapping("/project/{id}/collaborators")
    public String assignProjectCollaboratorsForm(@PathVariable Integer id, Model model) {
        Project project = projectService.findById(id);
        List<Collaborator> collaborators = collaboratorService.findAll();

        Map<Role, List<Collaborator>> roleCollaborators = getRoleListMap(project, collaborators);

        model.addAttribute("project", project);
        model.addAttribute("roleCollaborators", roleCollaborators);

        return "project_collaborators";
    }

    @RequestMapping(value = "/project/{id}/collaborators", method = RequestMethod.POST)
    public String assignCollaborators(@RequestParam Map<String, String> params, @PathVariable Integer id,
                                      RedirectAttributes redirectAttributes) {
        Project project = projectService.findById(id);
        assignCollaborators(params, project);

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Collaborators have been assigned.",
                FlashMessage.Status.SUCCESS));
        projectService.save(project);

        return String.format("redirect:/project/%s", id);
    }

    private Map<Role, Collaborator> getRoleCollaboratorMap(Project project) {
        List<Role> rolesNeeded = project.getRolesNeeded();
        List<Collaborator> collaborators = project.getCollaborators();
        Map<Role, Collaborator> roleCollaborator = new LinkedHashMap<>();

        for (Role roleNeeded : rolesNeeded) {
            roleCollaborator.put(roleNeeded,
                    collaborators.stream()
                            .filter( (col)-> col.getRole().getId().equals(roleNeeded.getId()))
                            .findFirst()
                            .orElseGet( () -> {
                                Collaborator unassigned = new Collaborator();
                                unassigned.setName("Unassigned");
                                return unassigned;
                            }));
        }

        return roleCollaborator;
    }

    private Map<Role, List<Collaborator>> getRoleListMap(Project project, List<Collaborator> collaborators) {
        Map<Role, List<Collaborator>> roleCollaborators = new HashMap<>();

        for (Role role : project.getRolesNeeded()) {
            List<Collaborator> coList = new ArrayList<>();

            for (Collaborator collaborator : collaborators) {
                if (collaborator.getRole() != null && collaborator.getRole().getId().equals(role.getId())) {
                    coList.add(collaborator);
                }
            }
            roleCollaborators.put(role, coList);
        }
        return roleCollaborators;
    }

    private void assignCollaborators(Map<String, String> params, Project project) {
        project.setCollaborators(new ArrayList<>());
        params.forEach( (key, value) -> {
            int collaboratorId = Integer.parseInt(value);

            // Unassign collaborator if id is 0
            if (collaboratorId == 0) {
                return;
            }

            // Check if collaborator is already assigned
            Collaborator collaborator = collaboratorService.findById(collaboratorId);
            List<Collaborator> collaborators = project.getCollaborators();
            if (collaborators
                    .stream()
                    .anyMatch( col -> col.getId().equals(collaborator.getId()))) {
                return;
            }

            // If not assign collaborator
            project.getCollaborators().add(collaborator);
        });
    }

}
