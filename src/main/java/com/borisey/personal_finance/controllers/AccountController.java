package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.Account;
import com.borisey.personal_finance.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/account/add")
    public String accountAccountAdd(@RequestParam String title) {
        Account account = new Account();

        // todo сделать проверку, что запись с таким названием не вносится повторно
        account.setTitle(title);

        // todo передавать ID реального пользователя
        account.setUserId(1L);

        // Сохраняю дату и время
        LocalDateTime currentDateTime = LocalDateTime.now();
        account.setCreated(currentDateTime);
        account.setUpdated(currentDateTime);

        accountRepository.save(account);

        return "redirect:/";
    }

}