package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.Account;
import com.borisey.personal_finance.models.User;
import com.borisey.personal_finance.repo.AccountRepository;
import com.borisey.personal_finance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/account/add")
    public String accountAccountAdd(@RequestParam String title, Float amount) {
        Account account = new Account();

        // todo сделать проверку, что запись с таким названием не вносится повторно
        account.setTitle(title);

        account.setAmount(amount);

        // Сохраняю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        account.setUserId(currentUser.getId());

        // Сохраняю дату и время
        LocalDateTime currentDateTime = LocalDateTime.now();
        account.setCreated(currentDateTime);
        account.setUpdated(currentDateTime);

        accountRepository.save(account);

        return "redirect:/my";
    }

}