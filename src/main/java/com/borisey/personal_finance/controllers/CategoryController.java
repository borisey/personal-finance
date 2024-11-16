package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.Account;
import com.borisey.personal_finance.models.Category;
import com.borisey.personal_finance.models.User;
import com.borisey.personal_finance.repo.CategoryRepository;
import com.borisey.personal_finance.services.UserService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserService userService;

    // Добавление новой категории доходов (зарплата, вклад) или расходов (еда, развлечения)
    @PostMapping("/category/add")
    public String accountAccountAdd(@RequestParam String title, @Nullable Byte typeId) {
        Category category = new Category();
        LocalDateTime currentDateTime = LocalDateTime.now();
        category.setTitle(title);
        category.setTypeId(typeId);

        // Сохраняю ID текущего пользователя
        User currentUser = userService.getCurrentUser();
        category.setUserId(currentUser.getId());

        // todo передавать ID родительской категории
        category.setParentId(null);
//        category.setAllamount(null);

        category.setCreated(currentDateTime);
        category.setUpdated(currentDateTime);
        categoryRepository.save(category);

        return "redirect:/my";
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

        return "category-edit";
    }

    @PostMapping("/category/{id}/edit")
    public String categoryEdit(@PathVariable(value = "id") Long id,
                              @RequestParam
                              String title
    ) {
        // todo проверять пользователя
        // Сохраняю категорию
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setTitle(title);

        // Меняю дату обновления
        LocalDateTime currentDateTime = LocalDateTime.now();
        category.setUpdated(currentDateTime);

        categoryRepository.save(category);

        return "redirect:/my";
    }

    @GetMapping("/category/{id}/delete")
    public String linkCategoryDelete(@PathVariable(value = "id") long id, Model model) {

        // todo искать также по ID пользователя, чтобы запретить удалить чужие записи
        Category category = categoryRepository.findById(id).orElseThrow();

        // todo доработать Если транзакцию добавил не этот пользователь
        categoryRepository.delete(category);

        return "redirect:/my";
    }
}