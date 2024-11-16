package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.*;
import com.borisey.personal_finance.repo.AccountRepository;
import com.borisey.personal_finance.repo.BalanceRepository;
import com.borisey.personal_finance.repo.CategoryRepository;
import com.borisey.personal_finance.repo.TypeRepository;
import com.borisey.personal_finance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    // Редактирование транзакции
    @GetMapping("/balance/{id}/edit")
    public String transactionEdit(@PathVariable(value = "id") Long id, Model model) {

        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        String username = currentUser.getUsername();

        // todo искать также по пользователю
        Balance transaction = balanceRepository.findById(id).orElseThrow();

        // todo Если статью добавил не этот пользователь (запретить редактирование)

        Optional<Balance> link = balanceRepository.findById(id);
        ArrayList<Balance> res = new ArrayList<>();
        link.ifPresent(res::add);
        model.addAttribute("transaction", res);

        // Счета

        // Передаю в вид все счета пользователя
        Iterable<Account> allUserAccounts = accountRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserAccounts", allUserAccounts);

        // Категории

        // Передаю в вид все категории доходов
        Iterable<Category> allUserIncomeCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 1, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserIncomeCategories", allUserIncomeCategories);

        // Передаю в вид все категории расходов
        Iterable<Category> allUserExpensesCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 2, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserExpensesCategories", allUserExpensesCategories);

        return "balance-edit";
    }

    @PostMapping("/balance/{id}/edit")
    public String transactionEdit(@PathVariable(value = "id") Long id,
                                 @RequestParam
                                 Long typeId,
                                 Float amount,
                                 Long categoryId,
                                 Long accountId,
                                 String date
    ) {
        Balance transaction = balanceRepository.findById(id).orElseThrow();

        // Сохраняю категорию
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        transaction.setCategory(category);

        // Сохраняю счет
        Account account = accountRepository.findById(accountId).orElseThrow();
        transaction.setAccount(account);

        // Сохраняю тип транзакции
        Type type = typeRepository.findById(typeId).orElseThrow();
        transaction.setType(type);

        // todo: у списания менять знак
        transaction.setAmount(amount);
        transaction.setDate(formatDate(date));

        // Сохраняю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        transaction.setUserId(currentUser.getId());

        // Сохраняю дату и время обновления записи
        LocalDateTime currentDateTime = LocalDateTime.now();
        transaction.setUpdated(currentDateTime);

        balanceRepository.save(transaction);

        return "redirect:/my";
    }

    @GetMapping("/balance/{id}/delete")
    public String linkLinkRemove(@PathVariable(value = "id") long id, Model model) {

        // todo искать также по ID пользователя, чтобы запретить удалить чужие записи
        Balance transaction = balanceRepository.findById(id).orElseThrow();

        // todo доработать Если транзакцию добавил не этот пользователь

        balanceRepository.delete(transaction);

        return "redirect:/my";
    }

    // Привожу строку с датой к формату LocalDateTime
    public LocalDateTime formatDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.parse(date + " 00:00:00", dateTimeFormatter);
    }

}