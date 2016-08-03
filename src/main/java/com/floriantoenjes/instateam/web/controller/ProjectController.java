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
    public String addProject(@RequestParam("project_roles") String roles, @Valid Project project, BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.project", result);
            redirectAttributes.addFlashAttribute("project", project);
            return "redirect:/add";
        }

        List<Role> rolesNeeded = parseRoles(roles);
        project.setRolesNeeded(rolesNeeded);

        redirectAttributes.addFlashAttribute("flash", new FlashMessage("Project created",
                FlashMessage.Status.SUCCESS));
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
        Project project = projectService.findById(id);
        List<Role> roles = roleService.findAll();

        model.addAttribute("project", project);
        model.addAttribute("roles", roles);

        return "edit_project";
    }

    @RequestMapping(value = "/project/{id}/edit", method = RequestMethod.POST)
    public String editProject(@PathVariable Integer id, @RequestParam("project_roles") String roles,
                              @Valid Project project, Model model) {
        List<Role> rolesNeeded = parseRoles(roles);
        project.setRolesNeeded(rolesNeeded);

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
    public String assignCollaborators(@RequestParam Map<String, String> params, @PathVariable Integer id) {
        Project project = projectService.findById(id);
        project.setCollaborators(new ArrayList<>());
        params.forEach( (key, value) -> {
            int collaboratorId = Integer.parseInt(value);
            if (collaboratorId == 0) {
                System.out.println("Unassigned the role");
                return;
            }
            Collaborator collaborator = collaboratorService.findById(collaboratorId);
            List<Collaborator> collaborators = project.getCollaborators();

            for (Collaborator colabProject : collaborators) {
                if (colabProject.getId() == collaborator.getId()) {
                    System.out.println("Did not add " + collaborator.getName());
                    return;
                }
            }

            project.getCollaborators().add(collaborator);
            System.out.println("Added " + collaborator.getName() + " to project " + project.getName());
        });
        projectService.save(project);

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

    private Map<Role, Collaborator> getRoleCollaboratorMap(Project project) {
        List<Role> rolesNeeded = project.getRolesNeeded();
        List<Collaborator> collaborators = project.getCollaborators();
        Map<Role, Collaborator> roleColab = new HashMap<>();

        roleLoop:
        for (Role roleNeeded : rolesNeeded) {
            for (Collaborator collaborator : collaborators) {
                if (roleNeeded.getId() == collaborator.getRole().getId()) {
                    roleColab.put(roleNeeded, collaborator);
                    continue roleLoop;
                }
            }
            Collaborator unassigned = new Collaborator();
            unassigned.setName("Unassigned");
            roleColab.put(roleNeeded, unassigned);
        }
        return roleColab;
    }

    private Map<Role, List<Collaborator>> getRoleListMap(Project project, List<Collaborator> collaborators) {
        Map<Role, List<Collaborator>> roleCollaborators = new HashMap<>();
        for (Role role : project.getRolesNeeded()) {
            List<Collaborator> coList = new ArrayList<>();
            for (Collaborator collaborator : collaborators) {
                if(collaborator.getRole() == null) {
                    continue;
                } else if (collaborator.getRole().getId() == role.getId()) {
                    coList.add(collaborator);
                }
            }
            roleCollaborators.put(role, coList);
        }
        return roleCollaborators;
    }

}
