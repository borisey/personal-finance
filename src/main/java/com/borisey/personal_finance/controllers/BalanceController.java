package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.*;
import com.borisey.personal_finance.repo.AccountRepository;
import com.borisey.personal_finance.repo.BalanceRepository;
import com.borisey.personal_finance.repo.CategoryRepository;
import com.borisey.personal_finance.repo.TypeRepository;
import com.borisey.personal_finance.services.FormatService;
import com.borisey.personal_finance.services.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    // Страница счетов пользователя
    @GetMapping("/transactions")
    public String getTransactions(HttpServletRequest request, Model model) {

        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");
        LocalDateTime dateTimeFrom;
        LocalDateTime dateTimeTo;

        LocalDateTime currentDateTime = LocalDateTime.now();

        if (StringUtils.isEmpty(dateFrom)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dateFrom = currentDateTime.withDayOfMonth(1).format(formatter);
            dateTimeFrom = currentDateTime.withDayOfMonth(1);
        } else {
            dateTimeFrom = FormatService.formatDate(dateFrom);
        }

        if (StringUtils.isEmpty(dateTo)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dateTimeTo = currentDateTime;
            dateTo = currentDateTime.format(formatter);
        } else {
            dateTimeTo = FormatService.formatDate(dateTo);
        }

        // Передаю в вид даты для запроса аналитики
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);

        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        String username = currentUser.getUsername();

        // Передаю в вид все транзакции пользователя
        Iterable<Balance> allUserTransactions = balanceRepository.findByUserIdDateTimeFromDateTimeTo(userId, dateTimeFrom, dateTimeTo, Sort.by(Sort.Direction.DESC, "date", "id"));
        model.addAttribute("allUserTransactions", allUserTransactions);

        // Передаю в вид имя пользователя
        model.addAttribute("username", username);

        // Передаю в вид все счета пользователя
        Iterable<Account> allUserAccounts = accountRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserAccounts", allUserAccounts);

        // Передаю в вид все категории доходов
        Iterable<Category> allUserIncomeCategories = categoryRepository.findByUserIdAndTypeIdAmount(userId, Type.INCOME, dateTimeFrom, dateTimeTo, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserIncomeCategories", allUserIncomeCategories);

        // Передаю в вид все категории расходов
        Iterable<Category> allUserExpensesCategories = categoryRepository.findByUserIdAndTypeIdAmount(userId, Type.EXPENSE, dateTimeFrom, dateTimeTo, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserExpensesCategories", allUserExpensesCategories);

        // Передаю в вид метатэги
        model.addAttribute("h1", "Транзакции");
        model.addAttribute("metaTitle", "Транзакции");
        model.addAttribute("metaDescription", "Транзакции");
        model.addAttribute("metaKeywords", "Транзакции");

        return "transactions";
    }

    // Добавление транзакцию
    @GetMapping("/transaction/add")
    public String transactionAdd(Model model) {

        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        String username = currentUser.getUsername();

        // Передаю в вид имя пользователя
        model.addAttribute("username", username);

        // Передаю в вид метатэги
        model.addAttribute("h1", "Добавление транзакции");
        model.addAttribute("metaTitle", "Добавление транзакции");
        model.addAttribute("metaDescription", "Добавление транзакции");
        model.addAttribute("metaKeywords", "Добавление транзакции");

        return "transaction-add";
    }

    // Добавление дохода
    @GetMapping("/transaction/income/add")
    public String transactionIncomeAdd(HttpServletRequest request, Model model) {

        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");
        LocalDateTime dateTimeFrom;
        LocalDateTime dateTimeTo;

        LocalDateTime currentDateTime = LocalDateTime.now();

        if (StringUtils.isEmpty(dateFrom)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dateFrom = currentDateTime.withDayOfMonth(1).format(formatter);
            dateTimeFrom = currentDateTime.withDayOfMonth(1);
        } else {
            dateTimeFrom = FormatService.formatDate(dateFrom);
        }

        if (StringUtils.isEmpty(dateTo)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dateTimeTo = currentDateTime;
            dateTo = currentDateTime.format(formatter);
        } else {
            dateTimeTo = FormatService.formatDate(dateTo);
        }

        // Передаю в вид даты для запроса аналитики
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);

        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        String username = currentUser.getUsername();

        Type type = typeRepository.findById(Type.INCOME).orElseThrow();
        // Передаю в вид все транзакции пользователя
        Iterable<Balance> allUserTransactions = balanceRepository.findByUserIdTypeFromTo(userId, type, dateTimeFrom, dateTimeTo, Sort.by(Sort.Direction.DESC, "date", "id"));
        model.addAttribute("allUserTransactions", allUserTransactions);

        // Передаю в вид имя пользователя
        model.addAttribute("username", username);

        // Категории

        // Передаю в вид все категории доходов
        Iterable<Category> allUserIncomeCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 1, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserIncomeCategories", allUserIncomeCategories);

        // Счета

        // Передаю в вид все счета пользователя
        Iterable<Account> allUserAccounts = accountRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserAccounts", allUserAccounts);

        // Передаю в вид метатэги
        model.addAttribute("h1", "Добавление дохода");
        model.addAttribute("metaTitle", "Добавление дохода");
        model.addAttribute("metaDescription", "Добавление дохода");
        model.addAttribute("metaKeywords", "Добавление дохода");

        return "transaction-income-add";
    }

    // Добавление расхода
    @GetMapping("/transaction/expense/add")
    public String transactionExpenseAdd(HttpServletRequest request, Model model) {

        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");
        LocalDateTime dateTimeFrom;
        LocalDateTime dateTimeTo;

        LocalDateTime currentDateTime = LocalDateTime.now();

        if (StringUtils.isEmpty(dateFrom)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dateFrom = currentDateTime.withDayOfMonth(1).format(formatter);
            dateTimeFrom = currentDateTime.withDayOfMonth(1);
        } else {
            dateTimeFrom = FormatService.formatDate(dateFrom);
        }

        if (StringUtils.isEmpty(dateTo)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dateTimeTo = currentDateTime;
            dateTo = currentDateTime.format(formatter);
        } else {
            dateTimeTo = FormatService.formatDate(dateTo);
        }

        // Передаю в вид даты для запроса аналитики
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);

        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        String username = currentUser.getUsername();

        // Передаю в вид имя пользователя
        model.addAttribute("username", username);

        Type type = typeRepository.findById(Type.EXPENSE).orElseThrow();
        // Передаю в вид все транзакции пользователя
        Iterable<Balance> allUserTransactions = balanceRepository.findByUserIdTypeFromTo(userId, type, dateTimeFrom, dateTimeTo, Sort.by(Sort.Direction.DESC, "date", "id"));
        model.addAttribute("allUserTransactions", allUserTransactions);

        // Категории

        // Передаю в вид все категории расходов
        Iterable<Category> allUserExpensesCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 2, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserExpensesCategories", allUserExpensesCategories);

        // Счета

        // Передаю в вид все счета пользователя
        Iterable<Account> allUserAccounts = accountRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserAccounts", allUserAccounts);

        // Передаю в вид метатэги
        model.addAttribute("h1", "Добавление расхода");
        model.addAttribute("metaTitle", "Добавление расхода");
        model.addAttribute("metaDescription", "Добавление расхода");
        model.addAttribute("metaKeywords", "Добавление расхода");

        return "transaction-expense-add";
    }

    // Пополнение
    @PostMapping("/transaction/add-income")
    public String balanceAddIncome(
            HttpServletRequest request,
            @RequestParam Long categoryId,
            Long accountId,
            Double amount,
            String date
    ) {
        Balance balance = new Balance();

        // Сохраняю категорию
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        balance.setCategory(category);

        // Сохраняю счет
        Account account = accountRepository.findById(accountId).orElseThrow();
        balance.setAccount(account);

        // Пополняю счет
        account.setAmount(account.getAmount() + amount);
        accountRepository.save(account);

        // Сохраняю сумму
        balance.setAmount(amount);
        balance.setDate(FormatService.formatDate(date));

        Type type = typeRepository.findById(Type.INCOME).orElseThrow();
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

        String referrer = request.getHeader("Referer");

        return "redirect:" + referrer;
    }

    // Списание
    @PostMapping("/transaction/add-expense")
    public String balanceAddExpense(
            HttpServletRequest request,
            @RequestParam Long categoryId,
            Long accountId,
            String date,
            Double amount
    ) {
        Balance balance = new Balance();

        // Сохраняю категорию
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        balance.setCategory(category);

        // Сохраняю счет
        Account account = accountRepository.findById(accountId).orElseThrow();
        balance.setAccount(account);

        // Списываю сумму со счета
        account.setAmount(account.getAmount() - amount);
        accountRepository.save(account);

        // Списание с отрицательным знаком
        balance.setAmount(-amount);
        balance.setDate(FormatService.formatDate(date));

        Type type = typeRepository.findById(Type.EXPENSE).orElseThrow();
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

        String referrer = request.getHeader("Referer");

        return "redirect:" + referrer;
    }

    // Редактирование дохода
    @GetMapping("/transaction/income/{id}/edit")
    public String incomeEdit(
            @PathVariable(value = "id") Long id,
            Model model
    ) {
        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        String username = currentUser.getUsername();

        // Пользователь не может редактировать чужие записи
        Balance transaction = balanceRepository.findByIdAndUserId(id, userId).orElseThrow();
        model.addAttribute("transaction", transaction);

        // Счета

        // Передаю в вид все счета пользователя
        Iterable<Account> allUserAccounts = accountRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserAccounts", allUserAccounts);

        // Категории

        // Передаю в вид все категории доходов
        Iterable<Category> allUserIncomeCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 1, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserIncomeCategories", allUserIncomeCategories);

        // Передаю в вид имя пользователя
        model.addAttribute("username", username);

        // Передаю в вид метатэги
        model.addAttribute("h1", "Редактирование дохода");
        model.addAttribute("metaTitle", "Редактирование дохода");
        model.addAttribute("metaDescription", "Редактирование дохода");
        model.addAttribute("metaKeywords", "Редактирование дохода");

        return "transaction-income-edit";
    }

    @PostMapping("/transaction/income/{id}/edit")
    public String incomeEdit(
            HttpServletRequest request,
            @PathVariable(value = "id") Long id,
            @RequestParam
            Long typeId,
            Double amount,
            Long categoryId,
            Long accountId,
            String date
    ) {
        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();

        // Пользователь не может редактировать чужие записи
        Balance transaction = balanceRepository.findByIdAndUserId(id, userId).orElseThrow();

        // Сохраняю категорию
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        transaction.setCategory(category);

        // Сохраняю счет
        Account account = accountRepository.findById(accountId).orElseThrow();
        transaction.setAccount(account);

        // Пополняю сумму на счет
        account.setAmount(account.getAmount() + amount);
        accountRepository.save(account);

        // Сохраняю тип транзакции
        Type type = typeRepository.findById(typeId).orElseThrow();
        transaction.setType(type);

        transaction.setAmount(amount);
        transaction.setDate(FormatService.formatDate(date));

        // Сохраняю ID текущего пользователя
        transaction.setUserId(currentUser.getId());

        // Сохраняю дату и время обновления записи
        LocalDateTime currentDateTime = LocalDateTime.now();
        transaction.setUpdated(currentDateTime);

        balanceRepository.save(transaction);

        String referrer = request.getHeader("Referer");

        return "redirect:" + referrer;
    }

    // Редактирование расхода
    @GetMapping("/transaction/expense/{id}/edit")
    public String expenseEdit(
            @PathVariable(value = "id") Long id,
            Model model
    ) {
        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        String username = currentUser.getUsername();

        // Пользователь не может редактировать чужие записи
        Balance transaction = balanceRepository.findByIdAndUserId(id, userId).orElseThrow();

        transaction.setAmount(-transaction.getAmount());

        model.addAttribute("transaction", transaction);

        // Счета

        // Передаю в вид все счета пользователя
        Iterable<Account> allUserAccounts = accountRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserAccounts", allUserAccounts);

        // Категории
        // Передаю в вид все категории расходов
        Iterable<Category> allUserExpensesCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 2, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserExpensesCategories", allUserExpensesCategories);

        // Передаю в вид имя пользователя
        model.addAttribute("username", username);

        // Передаю в вид метатэги
        model.addAttribute("h1", "Редактирование расхода");
        model.addAttribute("metaTitle", "Редактирование расхода");
        model.addAttribute("metaDescription", "Редактирование расхода");
        model.addAttribute("metaKeywords", "Редактирование расхода");

        return "transaction-expense-edit";
    }

    // Изменение расхода
    @PostMapping("/transaction/expense/{id}/edit")
    public String expenseEdit(
            HttpServletRequest request,
            @PathVariable(value = "id") Long id,
            @RequestParam
            Long typeId,
            Double amount,
            Long categoryId,
            Long accountId,
            String date
    ) {
        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();

        // Пользователь не может редактировать чужие записи
        Balance transaction = balanceRepository.findByIdAndUserId(id, userId).orElseThrow();

        // Сохраняю категорию
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        transaction.setCategory(category);

        // Сохраняю счет
        Account account = accountRepository.findById(accountId).orElseThrow();
        transaction.setAccount(account);

        // Списываю сумму со счета
        account.setAmount(account.getAmount() - amount);
        accountRepository.save(account);

        // Сохраняю тип транзакции
        Type type = typeRepository.findById(typeId).orElseThrow();
        transaction.setType(type);

        // У списания меняется знак
        transaction.setAmount(-amount);
        transaction.setDate(FormatService.formatDate(date));

        // Сохраняю ID текущего пользователя
        transaction.setUserId(currentUser.getId());

        // Сохраняю дату и время обновления записи
        LocalDateTime currentDateTime = LocalDateTime.now();
        transaction.setUpdated(currentDateTime);

        balanceRepository.save(transaction);

        String referrer = request.getHeader("Referer");

        return "redirect:" + referrer;
    }

    @GetMapping("/transaction/{id}/delete")
    public String transactionDelete(
            HttpServletRequest request,
            @PathVariable(value = "id") long id
    ) {
        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();

        // Пользователь не может удалять чужие записи
        Balance transaction = balanceRepository.findByIdAndUserId(id, userId).orElseThrow();

        try {
            balanceRepository.delete(transaction);
        } catch (Exception e) {
            return "redirect:/deletion-disallow";
        }

        String referrer = request.getHeader("Referer");

        return "redirect:" + referrer;
    }
}