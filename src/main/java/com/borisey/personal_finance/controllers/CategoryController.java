package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.Category;
import com.borisey.personal_finance.repo.CategoryRepository;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // Добавление новой категории доходов (зарплата, вклад) или расходов (еда, развлечения)
    @PostMapping("/category/add")
    public String accountAccountAdd(@RequestParam String title, @Nullable Byte typeId) {
        Category category = new Category();
        LocalDateTime currentDateTime = LocalDateTime.now();
        category.setTitle(title);
        category.setTypeId(typeId);

        // todo передавать ID реального пользователя
        category.setUserId(1L);

        // todo передавать ID родительской категории
        category.setParentId(null);

        category.setCreated(currentDateTime);
        category.setUpdated(currentDateTime);
        categoryRepository.save(category);

        return "redirect:/my";
    }
}