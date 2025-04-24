package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.user.UserService;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute User user, Model model, HttpServletRequest request) {
        var userOptional = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Invalid email or password");
            model.addAttribute("user", new User(0, "Гость", "", ""));
            return "users/login";
        }
        request.getSession().setAttribute("user", userOptional.get());
        return "redirect:/sessions";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute User user, Model model, HttpServletRequest request) {
        var userOptional = userService.save(user);
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "User with e-mail '%s' already exists".formatted(user.getEmail()));
            model.addAttribute("user", new User(0, "Гость", "", ""));
            return "users/register";
        }
        request.getSession().setAttribute("user", userOptional.get());
        return "redirect:/users/login";
    }

}
