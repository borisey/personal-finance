package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.Account;
import com.borisey.personal_finance.models.Category;
import com.borisey.personal_finance.models.User;
import com.borisey.personal_finance.repo.CategoryRepository;
import com.borisey.personal_finance.services.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
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
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserService userService;

    // Страница категорий пользователя
    @GetMapping("/categories")
    public String getCategories(Model model) {
        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        String username = currentUser.getUsername();

        // Передаю в вид все категории доходов todo сделать константу
        Iterable<Category> allUserIncomeCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 1, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserIncomeCategories", allUserIncomeCategories);

        // Передаю в вид все категории расходов todo сделать константу
        Iterable<Category> allUserExpensesCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 2, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserExpensesCategories", allUserExpensesCategories);

        // Передаю в вид имя пользователя
        model.addAttribute("username", username);

        // Передаю в вид метатэги
        model.addAttribute("h1", "Категории");
        model.addAttribute("metaTitle", "Категории");
        model.addAttribute("metaDescription", "Категории");
        model.addAttribute("metaKeywords", "Категории");

        return "categories";
    }

    // Добавление категории
    @GetMapping("/category/add")
    public String categoryAdd(Model model) {

        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        String username = currentUser.getUsername();

        // Передаю в вид имя пользователя
        model.addAttribute("username", username);

        // Передаю в вид метатэги
        model.addAttribute("h1", "Добавление категории");
        model.addAttribute("metaTitle", "Добавление категории");
        model.addAttribute("metaDescription", "Добавление категории");
        model.addAttribute("metaKeywords", "Добавление категории");

        return "category-add";
    }

    // Добавление новой категории доходов (зарплата, вклад) или расходов (еда, развлечения)
    @PostMapping("/category/add")
    public String categoryAdd(
            HttpServletRequest request,
            @RequestParam String title, Double budget, @Nullable Byte typeId
    )
    {
        Category category = new Category();
        LocalDateTime currentDateTime = LocalDateTime.now();
        category.setTitle(title);
        category.setTypeId(typeId);
        category.setBudget(budget);

        // Сохраняю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        category.setUserId(currentUser.getId());

        // todo передавать ID родительской категории
        category.setParentId(null);
//        category.setAllamount(null);

        category.setCreated(currentDateTime);
        category.setUpdated(currentDateTime);
        categoryRepository.save(category);

        String referrer = request.getHeader("Referer");

        return "redirect:" + referrer;
    }

    // Добавление категории дохода
    @GetMapping("/category/income/add")
    public String categoryIncomeAdd(Model model) {

        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        String username = currentUser.getUsername();

        // Передаю в вид имя пользователя
        model.addAttribute("username", username);

        // Передаю в вид все категории доходов
        Iterable<Category> allUserIncomeCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 1, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserIncomeCategories", allUserIncomeCategories);

        // Передаю в вид метатэги
        model.addAttribute("h1", "Добавление категории дохода");
        model.addAttribute("metaTitle", "Добавление категории дохода");
        model.addAttribute("metaDescription", "Добавление категории дохода");
        model.addAttribute("metaKeywords", "Добавление категории дохода");

        return "category-income-add";
    }

    // Добавление категории расхода
    @GetMapping("/category/expense/add")
    public String categoryExpenseAdd(Model model) {

        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        String username = currentUser.getUsername();

        // Передаю в вид имя пользователя
        model.addAttribute("username", username);

        // Передаю в вид все категории доходов
        Iterable<Category> allUserExpensesCategories = categoryRepository.findByUserIdAndTypeId(userId, (byte) 2, Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allUserExpensesCategories", allUserExpensesCategories);

        // Передаю в вид метатэги
        model.addAttribute("h1", "Добавление категории расхода");
        model.addAttribute("metaTitle", "Добавление категории расхода");
        model.addAttribute("metaDescription", "Добавление категории расхода");
        model.addAttribute("metaKeywords", "Добавление категории расхода");

        return "category-expense-add";
    }

    // Редактирование счета
    @GetMapping("/category/{id}/edit")
    public String categoryEdit(@PathVariable(value = "id") Long id, Model model) {

        // Получаю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        Long userId = currentUser.getId();
        String username = currentUser.getUsername();

        // todo искать также по пользователю
        Category transaction = categoryRepository.findById(id).orElseThrow();

        // todo Если статью добавил не этот пользователь (запретить редактирование)

        Optional<Category> link = categoryRepository.findById(id);
        ArrayList<Category> res = new ArrayList<>();
        link.ifPresent(res::add);
        model.addAttribute("categories", res);

        // Передаю в вид имя пользователя
        model.addAttribute("username", username);

        // Передаю в вид метатэги
        model.addAttribute("h1", "Редактирование категории");
        model.addAttribute("metaTitle", "Редактирование категории");
        model.addAttribute("metaDescription", "Редактирование категории");
        model.addAttribute("metaKeywords", "Редактирование категории");

        return "category-edit";
    }

    @PostMapping("/category/{id}/edit")
    public String categoryEdit(
            HttpServletRequest request,
            @PathVariable(value = "id") Long id, Double budget,
            @RequestParam
            String title
    ) {
        // todo проверять пользователя
        // Сохраняю категорию
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setTitle(title);
        category.setBudget(budget);

        // Меняю дату обновления
        LocalDateTime currentDateTime = LocalDateTime.now();
        category.setUpdated(currentDateTime);

        categoryRepository.save(category);

        String referrer = request.getHeader("Referer");

        return "redirect:" + referrer;
    }

    @GetMapping("/category/{id}/delete")
    public String linkCategoryDelete(
            HttpServletRequest request,
            @PathVariable(value = "id") long id,
            Model model
    ) {

        // todo искать также по ID пользователя, чтобы запретить удалить чужие записи
        Category category = categoryRepository.findById(id).orElseThrow();

        // todo доработать Если транзакцию добавил не этот пользователь
        categoryRepository.delete(category);

        String referrer = request.getHeader("Referer");

        return "redirect:" + referrer;
    }
}