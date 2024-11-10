package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.*;
import com.borisey.personal_finance.repo.AccountRepository;
import com.borisey.personal_finance.repo.BalanceRepository;
import com.borisey.personal_finance.repo.CategoryRepository;
import com.borisey.personal_finance.repo.TypeRepository;
import com.borisey.personal_finance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class BalanceController {

    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserService userService;

    // Пополнение
    @PostMapping("/balance/add-income")
    public String balanceAddIncome(@RequestParam Long categoryId, Long accountId, Float amount, String date) {
        Balance balance = new Balance();

        // Сохраняю категорию
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        balance.setCategory(category);

        // Сохраняю счет
        Account account = accountRepository.findById(accountId).orElseThrow();
        balance.setAccount(account);

        // Сохраняю сумму
        balance.setAmount(amount);
        balance.setDate(formatDate(date));

        // todo доход сделать константу
        Type type = typeRepository.findById(1L).orElseThrow();
        balance.setType(type);

        // Сохраняю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        balance.setUserId(currentUser.getId());

        // Сохраняю дату и время
        LocalDateTime currentDateTime = LocalDateTime.now();
        balance.setCreated(currentDateTime);
        balance.setUpdated(currentDateTime);

        // todo сделать проверку, что запись не вносится повторно
        balanceRepository.save(balance);

        return "redirect:/my";
    }

    // Списание
    @PostMapping("/balance/add-expense")
    public String balanceAddExpense(@RequestParam Long categoryId, Long accountId, String date, Float amount) {
        Balance balance = new Balance();

        // Сохраняю категорию
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        balance.setCategory(category);

        // Сохраняю счет
        Account account = accountRepository.findById(accountId).orElseThrow();
        balance.setAccount(account);

        // Списание с отрицательным знаком
        balance.setAmount(-amount);
        balance.setDate(formatDate(date));

        // todo расход сделать константу
        Type type = typeRepository.findById(2L).orElseThrow();
        balance.setType(type);

        // Сохраняю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        balance.setUserId(currentUser.getId());

        // Сохраняю дату и время
        LocalDateTime currentDateTime = LocalDateTime.now();
        balance.setCreated(currentDateTime);
        balance.setUpdated(currentDateTime);

        // todo сделать проверку, что запись не вносится повторно
        balanceRepository.save(balance);

        return "redirect:/my";
    }

    // Привожу строку с датой к формату LocalDateTime
    public LocalDateTime formatDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.parse(date + " 00:00:00", dateTimeFormatter);
    }

}