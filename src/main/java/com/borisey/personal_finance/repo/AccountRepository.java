package com.borisey.personal_finance.repo;

import com.borisey.personal_finance.models.Account;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Iterable<Account> findAll(Sort colName);
    Account findByShortUrl(String shortUrl);
    Iterable<Account> findByUUID(String UUID, Sort colName);
    Account findByIdAndUUID(Long id, String UUID);
}
