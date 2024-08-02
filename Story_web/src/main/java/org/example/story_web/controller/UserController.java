package org.example.story_web.controller;


import lombok.RequiredArgsConstructor;
import org.example.story_web.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/user/index";
    }

    @GetMapping("/create")
    public String getCreate() {
        return "admin/user/create";
    }

    @GetMapping("/{id}/detail")
    public String getDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "admin/user/detail";
    }
}
