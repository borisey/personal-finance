package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.Account;
import com.borisey.personal_finance.models.Category;
import com.borisey.personal_finance.models.User;
import com.borisey.personal_finance.repo.AccountRepository;
import com.borisey.personal_finance.repo.CategoryRepository;
import com.borisey.personal_finance.repo.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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

        return "redirect:/";
    }
}