package com.floriantoenjes.instateam.web.controller;

import com.floriantoenjes.instateam.model.Role;
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
import java.util.List;

@Controller
public class RoleController {
    @Autowired
    RoleService roleService;

    @RequestMapping("/roles")
    public String listRoles(Model model) {
        List<Role> roles = roleService.findAll();
        if (!model.containsAttribute("role")) {
            model.addAttribute("role", new Role());
        }
        model.addAttribute("roles", roles);

        return "roles";
    }

    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public String addRole(@Valid Role role, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.role",result);
            redirectAttributes.addFlashAttribute("role", role);
            /*redirectAttributes.addFlashAttribute("flash", new FlashMessage("Role could not be created",
                    FlashMessage.Status.FAILED));*/
        } else {
            redirectAttributes.addFlashAttribute("flash", new FlashMessage("Role has been created",
                    FlashMessage.Status.SUCCESS));
            roleService.save(role);
        }
        return "redirect:/roles";
    }

    @RequestMapping("/roles/{id}")
    public String editRoleForm(@PathVariable Integer id, Model model) {
        Role role = roleService.findById(id);

        model.addAttribute("role", role);

        return "role_detail";
    }
}
