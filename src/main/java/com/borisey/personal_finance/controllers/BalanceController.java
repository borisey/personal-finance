package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.Balance;
import com.borisey.personal_finance.models.Type;
import com.borisey.personal_finance.repo.BalanceRepository;
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

    // Пополнение
    @PostMapping("/balance/add-income")
    public String balanceAddIncome(@RequestParam Long categoryId, Long accountId, Float amount, String date) {
        Balance balance = new Balance();
        balance.setCategoryId(categoryId);
        balance.setAccountId(accountId);
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

        balance.setCategoryId(categoryId);
        balance.setAccountId(accountId);

        balance.setAmount(-amount); // Списание с отрицательным знаком
        balance.setDate(formatDate(date));

        Type type = typeRepository.findById(2L).orElseThrow(); // todo доход сделать константу
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