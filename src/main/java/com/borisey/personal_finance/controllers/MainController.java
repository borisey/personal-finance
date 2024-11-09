package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.Account;
import com.borisey.personal_finance.models.Category;
import com.borisey.personal_finance.models.User;
import com.borisey.personal_finance.repo.AccountRepository;
import com.borisey.personal_finance.repo.CategoryRepository;
import com.borisey.personal_finance.repo.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

        // Категории

        // Передаю в вид все категории доходов
        // todo передавать ID реального пользователя
        Iterable<Category> allUserIncomeCategories = categoryRepository.findByUserIdAndTypeId(1L, (byte) 1, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserIncomeCategories", allUserIncomeCategories);

        // Передаю в вид все категории расходов
        // todo передавать ID реального пользователя
        Iterable<Category> allUserExpensesCategories = categoryRepository.findByUserIdAndTypeId(1L, (byte) 2, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserExpensesCategories", allUserExpensesCategories);

        // Счета

        // Передаю в вид все счета пользователя
        // todo передавать ID реального пользователя
        Iterable<Account> allUserAccounts = accountRepository.findByUserId(1L, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserAccounts", allUserAccounts);

        // Общая сумма на всех счетах
        Iterable<Account> allUserAmount = accountRepository.findSumByUserId(1L);
        model.addAttribute("allUserAmount", allUserAmount);

        return "home";
    }
}