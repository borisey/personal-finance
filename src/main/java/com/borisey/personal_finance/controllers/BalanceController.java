package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.Account;
import com.borisey.personal_finance.models.Balance;
import com.borisey.personal_finance.models.Category;
import com.borisey.personal_finance.models.Type;
import com.borisey.personal_finance.repo.AccountRepository;
import com.borisey.personal_finance.repo.BalanceRepository;
import com.borisey.personal_finance.repo.CategoryRepository;
import com.borisey.personal_finance.repo.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

        Type type = typeRepository.findById(1L).orElseThrow(); // todo доход сделать константу
        balance.setType(type);

        // todo передавать ID реального пользователя
        balance.setUserId(1L);

        // Сохраняю дату и время
        LocalDateTime currentDateTime = LocalDateTime.now();
        balance.setCreated(currentDateTime);
        balance.setUpdated(currentDateTime);

        // todo сделать проверку, что запись не вносится повторно
        balanceRepository.save(balance);

        return "redirect:/";
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

        balance.setAmount(-amount); // Списание с отрицательным знаком
        balance.setDate(formatDate(date));

        Type type = typeRepository.findById(2L).orElseThrow(); // todo расход сделать константу
        balance.setType(type);

        // todo передавать ID реального пользователя
        balance.setUserId(1L);

        // Сохраняю дату и время
        LocalDateTime currentDateTime = LocalDateTime.now();
        balance.setCreated(currentDateTime);
        balance.setUpdated(currentDateTime);

        // todo сделать проверку, что запись не вносится повторно
        balanceRepository.save(balance);

        return "redirect:/";
    }

    // Привожу строку с датой к формату LocalDateTime
    public LocalDateTime formatDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.parse(date + " 00:00:00", dateTimeFormatter);
    }

}