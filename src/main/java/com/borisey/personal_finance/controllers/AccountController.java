package com.borisey.personal_finance.controllers;

import com.borisey.personal_finance.models.Account;
import com.borisey.personal_finance.models.User;
import com.borisey.personal_finance.repo.AccountRepository;
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
import java.util.*;

@Controller
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/account")
    public String account(@CookieValue(value = "UUID", defaultValue = "") String UUID, HttpServletRequest request, Model model) {
        Iterable<Account> accounts = accountRepository.findByUUID(UUID, Sort.by(Sort.Direction.DESC, "id"));
        String baseUrl = getBaseUrl(request);
        model.addAttribute("accounts", accounts);
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("UUID", UUID);
        return "account";
    }

    @GetMapping("/account/time-expired")
    public String accountTimeExpired(Model model) {

        return "time-expired";
    }

    @PostMapping("/account/add")
    public String accountAccountAdd(@CookieValue(value = "UUID", defaultValue = "") String UUID, HttpServletResponse response, @RequestParam String fullUrl, @Nullable Integer count, Model model) {
        Account account = new Account(fullUrl);
        LocalDateTime currentDateTime = LocalDateTime.now();

        String randomString = usingUUID();
        String shortUrl = randomString.substring(0, 6);
        account.setShortUrl(shortUrl);
        account.setCount(count);
        account.setCreated(currentDateTime);
        accountRepository.save(account);

        // Генерирую UUID только новым пользователям
        if (Objects.equals(UUID, "")) {
            UUID = randomString.substring(0, 20);
            User user = new User();
            user.setUUID(UUID);
            userRepository.save(user);

            // Сохраняю UUID в cookie
            Cookie cookie = new Cookie("UUID", UUID);
            cookie.setPath("/"); // global cookie accessible
            // Добавляю файл cookie в ответ сервера
            response.addCookie(cookie);
        }

        account.setUUID(UUID);
        accountRepository.save(account);

        return "redirect:/account";
    }

    @GetMapping("/account/limit-reached")
    public String accountLimitReached(Model model) {
        return "limit-reached";
    }

    @GetMapping("/account/not-found")
    public String accountNotFound(Model model) {
        return "not-found";
    }

    @GetMapping("/account/edit-failed")
    public String editFailed(Model model) {
        return "edit-failed";
    }

    @GetMapping("/account/delete-failed")
    public String deleteFailed(Model model) {
        return "delete-failed";
    }

    public static String getBaseUrl(HttpServletRequest request) {
        String baseUrl = ServletUriComponentsBuilder
                .fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();

        return baseUrl;
    }

    static String usingUUID() {
        UUID randomUUID = UUID.randomUUID();
        return randomUUID.toString().replaceAll("-", "");
    }

    @GetMapping("/account/{id}")
    public String accountDetails(@PathVariable(value = "id") long id, Model model) {
        if(!accountRepository.existsById(id)) {
            return "redirect:/account";

        }
        Optional<Account> account = accountRepository.findById(id);
        ArrayList<Account> res = new ArrayList<>();
        account.ifPresent(res::add);
        model.addAttribute("account", res);
        return "account-details";
    }

    @GetMapping("/account/{id}/edit")
    public String accountEdit(@CookieValue(value = "UUID", defaultValue = "") String UUID, @PathVariable(value = "id") long id, Model model) {

        Account userAccount = accountRepository.findByIdAndUUID(id, UUID);

        // Если статью добавил не этот пользователь
        if (userAccount == null) {
            return "redirect:/account/edit-failed";
        }

        Optional<Account> account = accountRepository.findById(id);
        ArrayList<Account> res = new ArrayList<>();
        account.ifPresent(res::add);
        model.addAttribute("account", res);
        model.addAttribute("UUID", UUID);
        return "account-edit";
    }

    @PostMapping("/account/{id}/edit")
    public String accountAccountUpdate(@PathVariable(value = "id") long id, @RequestParam String fullUrl, Integer count, Model model) {
        Account account = accountRepository.findById(id).orElseThrow();
        account.setFullUrl(fullUrl);
        account.setCount(count);
        accountRepository.save(account);

        return "redirect:/account";
    }

    @GetMapping("/account/{id}/delete")
    public String accountAccountRemove(@CookieValue(value = "UUID", defaultValue = "") String UUID, @PathVariable(value = "id") long id, Model model) {

        Account userAccount = accountRepository.findByIdAndUUID(id, UUID);

        // Если статью добавил не этот пользователь
        if (userAccount == null) {
            return "redirect:/account/delete-failed";
        }

        accountRepository.delete(userAccount);

        return "redirect:/account";
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> accountRedirect(HttpServletRequest request, @PathVariable(value = "shortUrl") String shortUrl, Model model) throws ParseException {
        Account account = accountRepository.findByShortUrl(shortUrl);

        String baseUrl = getBaseUrl(request);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(baseUrl + "/account/not-found")).build();
        }

        LocalDateTime created = account.getCreated()
                .plusDays(1);

        LocalDateTime now = LocalDateTime.now();
        boolean isAfter = now.isAfter(created);

        if (isAfter) {
            accountRepository.delete(account);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(baseUrl + "/account/time-expired")).build();
        }

        Integer count = account.getCount();

        if (count != null) {
            if (count == 0) {
                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(baseUrl + "/account/limit-reached")).build();
            }

            account.setCount(--count);
            accountRepository.save(account);

            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(account.getFullUrl())).build();
        }

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(account.getFullUrl())).build();
    }

}