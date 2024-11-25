package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.*;
import com.borisey.personal_finance.repo.*;
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

    @GetMapping({"/my/dateFrom={dateFrom}&dateTo={dateTo}","/my"})
    public String myPage(
            HttpServletRequest request,
            Model model
    ) {
        // todo вынести метод в другой класс
        BalanceController balanceController = new BalanceController();

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
            dateTimeFrom = balanceController.formatDate(dateFrom);
        }

        if (StringUtils.isEmpty(dateTo)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dateTimeTo = currentDateTime;
            dateTo = currentDateTime.format(formatter);
        } else {
            dateTimeTo = balanceController.formatDate(dateTo);
        }

        // Передаю в вид даты для запроса аналитики
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);

        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        String username = currentUser.getUsername();

        // Категории

        // Передаю в вид все категории доходов todo сделать константу
        Iterable<Category> allUserIncomeCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 1, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserIncomeCategories", allUserIncomeCategories);

        // Передаю в вид все категории расходов todo сделать константу
        Iterable<Category> allUserExpensesCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 2, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserExpensesCategories", allUserExpensesCategories);

        // Счета

        // Передаю в вид все счета пользователя
        Iterable<Account> allUserAccounts = accountRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserAccounts", allUserAccounts);

        // Общая сумма доходов
        Type typeIncome = typeRepository.findById(1L).orElseThrow(); // todo сделать константу
        Iterable<Balance> allUserIncome = balanceRepository.findSumByUserIdTypeIdDateTimeFromDateTimeTo(userId, typeIncome, dateTimeFrom, dateTimeTo);
        model.addAttribute("allUserIncome", allUserIncome);

        // Общая сумма расходов
        Type typeExpense = typeRepository.findById(2L).orElseThrow(); // todo сделать константу
        Iterable<Balance> allUserExpense = balanceRepository.findSumByUserIdTypeIdDateTimeFromDateTimeTo(userId, typeExpense, dateTimeFrom, dateTimeTo);
        model.addAttribute("allUserExpense", allUserExpense);

        // Общая сумма на всех счетах
        Iterable<Account> allUserAmount = accountRepository.findSumByUserId(userId);
        model.addAttribute("allUserAmount", allUserAmount);

        // Передаю в вид все транзакции пользователя
        Iterable<Balance> allUserTransactions = balanceRepository.findByUserIdDateTimeFromDateTimeTo(userId, dateTimeFrom, dateTimeTo, Sort.by(Sort.Direction.DESC, "date", "id"));
        model.addAttribute("allUserTransactions", allUserTransactions);

        // Передаю в вид имя пользователя
        model.addAttribute("username", username);

        // Передаю в вид метатэги
        model.addAttribute("h1", "Дашборд");
        model.addAttribute("metaTitle", "Дашборд");
        model.addAttribute("metaDescription", "Дашборд");
        model.addAttribute("metaKeywords", "Дашборд");

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