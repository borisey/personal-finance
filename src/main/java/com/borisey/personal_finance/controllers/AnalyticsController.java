package com.borisey.personal_finance.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.borisey.personal_finance.models.Category;
import com.borisey.personal_finance.models.User;
import com.borisey.personal_finance.repo.CategoryRepository;
import com.borisey.personal_finance.services.UserService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AnalyticsController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserService userService;

    @GetMapping({"/analytics/dateFrom={dateFrom}&dateTo={dateTo}","/analytics"})
    public String getAnalytics(
            @PathVariable(value = "dateFrom", required = false) String dateFrom,
            @PathVariable(value = "dateTo", required = false) String dateTo,
            Model model
    ) {
        // todo вынести метод в другой класс
        BalanceController balanceController = new BalanceController();

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime dateTimeFrom = StringUtils.isEmpty(dateFrom) ? currentDateTime.withDayOfMonth(1) : balanceController.formatDate(dateFrom);
        LocalDateTime dateTimeTo = StringUtils.isEmpty(dateTo) ? currentDateTime :  balanceController.formatDate(dateTo);

        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();

        // Передаю в вид имя пользователя
        String username = currentUser.getUsername();
        model.addAttribute("username", username);

        // Получаю все категории доходов
        Iterable<Category> allUserIncomeCategories = categoryRepository.findByUserIdAndTypeIdAmount(userId, (byte) 1, dateTimeFrom, dateTimeTo, Sort.by(Sort.Direction.DESC, "id"));

        Map<String, Double> chartIncomeData = new TreeMap<>();
        for (Category category : allUserIncomeCategories) {
            chartIncomeData.put(category.getTitle(), category.getAllamount());
        }

        // Передаю в вид доходы
        model.addAttribute("chartIncomeData", chartIncomeData);

        // Получаю все категории расходов
        Iterable<Category> allUserExpenseCategories = categoryRepository.findByUserIdAndTypeIdAmount(userId, (byte) 2, dateTimeFrom, dateTimeTo, Sort.by(Sort.Direction.DESC, "id"));

        Map<String, Double> chartExpenseData = new TreeMap<>();
        for (Category category : allUserExpenseCategories) {
            chartExpenseData.put(category.getTitle(), -category.getAllamount());
        }

        // Передаю в вид расходы
        model.addAttribute("chartExpenseData", chartExpenseData);

        // Передаю в вид метатэги
        model.addAttribute("h1", "Аналитика");
        model.addAttribute("metaTitle", "Аналитика");
        model.addAttribute("metaDescription", "Аналитика");
        model.addAttribute("metaKeywords", "Аналитика");

        return "analytics";
    }
}