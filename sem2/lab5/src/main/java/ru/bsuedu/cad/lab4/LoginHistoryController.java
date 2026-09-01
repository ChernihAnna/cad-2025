package ru.bsuedu.cad.lab4;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login-history")
public class LoginHistoryController {

    private final LoginHistoryService loginHistoryService;

    public LoginHistoryController(LoginHistoryService loginHistoryService) {
        this.loginHistoryService = loginHistoryService;
    }

    @GetMapping
    public String loginHistory(Model model) {
        model.addAttribute(
                "loginHistory",
                loginHistoryService.getAllLoginHistory()
        );

        return "login-history";
    }
}