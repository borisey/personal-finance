package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.*;
import com.borisey.personal_finance.repo.AccountRepository;
import com.borisey.personal_finance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/account/add")
    public String accountAccountAdd(@RequestParam String title, Double amount) {
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

    // Редактирование счета
    @GetMapping("/account/{id}/edit")
    public String accountEdit(@PathVariable(value = "id") Long id, Model model) {

        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        String username = currentUser.getUsername();

        // todo искать также по пользователю
        Account transaction = accountRepository.findById(id).orElseThrow();

        // todo Если статью добавил не этот пользователь (запретить редактирование)

        Optional<Account> link = accountRepository.findById(id);
        ArrayList<Account> res = new ArrayList<>();
        link.ifPresent(res::add);
        model.addAttribute("account", res);

        return "account-edit";
    }

    @PostMapping("/account/{id}/edit")
    public String accountEdit(@PathVariable(value = "id") Long id,
                             @RequestParam
                             Double amount,
                             String title
    ) {
        // todo проверять пользователя
        // Сохраняю счет
        Account account = accountRepository.findById(id).orElseThrow();
        account.setAmount(amount);
        account.setTitle(title);
        accountRepository.save(account);

        return "redirect:/my";
    }

    @GetMapping("/account/{id}/delete")
    public String linkLinkRemove(@PathVariable(value = "id") long id, Model model) {

        // todo искать также по ID пользователя, чтобы запретить удалить чужие записи
        Account account = accountRepository.findById(id).orElseThrow();

        // todo доработать Если транзакцию добавил не этот пользователь

        accountRepository.delete(account);

        return "redirect:/my";
    }

}