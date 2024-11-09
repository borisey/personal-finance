package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.Category;
import com.borisey.personal_finance.models.User;
import com.borisey.personal_finance.repo.AccountRepository;
import com.borisey.personal_finance.repo.CategoryRepository;
import com.borisey.personal_finance.repo.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
public class MainController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/")
    public String accountAdd(Model model) {

        // Передаю в вид все категории доходов
        // todo передавать ID реального пользователя
        Iterable<Category> allUserIncomeCategories = categoryRepository.findByUserIdAndTypeId(1L, (byte) 1);
        model.addAttribute("allUserIncomeCategories", allUserIncomeCategories);

        // Передаю в вид все категории расходов
        // todo передавать ID реального пользователя
        Iterable<Category> allUserExpensesCategories = categoryRepository.findByUserIdAndTypeId(1L, (byte) 2);
        model.addAttribute("allUserExpensesCategories", allUserExpensesCategories);

        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, Model model) {

        Cookie cookie = new Cookie("UUID", "");
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "home";
    }

    @GetMapping("/login")
    public String login(@CookieValue(value = "UUID", defaultValue = "") String UUID, HttpServletResponse response, Model model) {

        if (!Objects.equals(UUID, "")) {
            return "redirect:/";
        }

        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletResponse response, @RequestParam String UUID, Model model) {
        User userIdentity = userRepository.findByUUID(UUID);

        if (userIdentity == null) {
            return "redirect:/auth-failed";
        } else {
            Cookie cookie = new Cookie("UUID", UUID);
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/auth-success";
        }
    }

    @GetMapping("/auth-failed")
    public String authFailed(HttpServletResponse response, Model model) {
        return "auth-failed";
    }

    @GetMapping("/auth-success")
    public String authSuccess(@CookieValue(value = "UUID", defaultValue = "") String UUID, HttpServletResponse response, Model model) {

        model.addAttribute("UUID", UUID);
        return "auth-success";
    }
}