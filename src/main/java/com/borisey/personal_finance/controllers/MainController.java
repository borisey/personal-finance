package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.*;
import com.borisey.personal_finance.repo.*;
import com.borisey.personal_finance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private UserService userService;

    @GetMapping("/my")
    public String myPage(Model model) {

        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        String username = currentUser.getUsername();

        // Категории

        // Передаю в вид все категории доходов
        Iterable<Category> allUserIncomeCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 1, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserIncomeCategories", allUserIncomeCategories);

        // Передаю в вид все категории расходов
        Iterable<Category> allUserExpensesCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 2, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserExpensesCategories", allUserExpensesCategories);

        // Счета

        // Передаю в вид все счета пользователя
        Iterable<Account> allUserAccounts = accountRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserAccounts", allUserAccounts);

        // Общая сумма доходов
        Type typeIncome = typeRepository.findById(1L).orElseThrow(); // todo сделать константу
        Iterable<Balance> allUserIncome = balanceRepository.findSumByUserIdTypeId(userId, typeIncome);
        model.addAttribute("allUserIncome", allUserIncome);

        // Общая сумма расходов
        Type typeExpense = typeRepository.findById(2L).orElseThrow(); // todo сделать константу
        Iterable<Balance> allUserExpense = balanceRepository.findSumByUserIdTypeId(userId, typeExpense);
        model.addAttribute("allUserExpense", allUserExpense);

        // Общая сумма на всех счетах
        Iterable<Account> allUserAmount = accountRepository.findSumByUserId(userId);
        model.addAttribute("allUserAmount", allUserAmount);

        // Передаю в вид все транзакции пользователя
        Iterable<Balance> allUserTransactions = balanceRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "date", "id"));
        model.addAttribute("allUserTransactions", allUserTransactions);

        // Передаю в вид имя пользователя
        model.addAttribute("username", username);

        return "my";
    }

    @GetMapping("/")
    public String accountHome(Model model) {
        return "landing";
    }

    // todo перенести в registration controller
    @GetMapping("/register-success")
    public String registerSuccess() {
        return "register-success";
    }
}