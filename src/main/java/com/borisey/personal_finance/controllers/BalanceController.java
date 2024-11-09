package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.Balance;
import com.borisey.personal_finance.repo.BalanceRepository;
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

    @PostMapping("/balance/add-income")
    public String balanceAddIncome(@RequestParam Long categoryId, Long accountId, Float amount, String date) {

        // Привожу строку с датой к формату LocalDateTime
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateFormatted = LocalDateTime.parse(date + " 00:00:00", dateTimeFormatter);

        Balance balance = new Balance();

        balance.setCategoryId(categoryId);
        balance.setAccountId(accountId);
        balance.setAmount(amount);
        balance.setDate(dateFormatted);
        balance.setTypeId((byte) 1);

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

    @PostMapping("/balance/add-expense")
    public String balanceAddExpense(@RequestParam Long categoryId, Long accountId, LocalDateTime date, Float amount) {

        return "redirect:/";
    }

}