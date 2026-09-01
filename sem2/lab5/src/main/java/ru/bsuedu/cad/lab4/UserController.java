package ru.bsuedu.cad.lab4;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

  @PostMapping("/add")
public String addUser(
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String password,
        @RequestParam String role) {

    User user = new User();

    user.setName(username);
    user.setEmail(email);
    user.setPassword(password);
    user.setRole(role);

    userService.addUser(user);

    return "redirect:/users";
}

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}