package com.borisey.personal_finance.repo;

import com.borisey.personal_finance.models.Account;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Iterable<Account> findAll(Sort colName);
    Iterable<Account> findByUserId(Long userId);
}
