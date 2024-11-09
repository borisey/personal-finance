package com.borisey.personal_finance.repo;

import com.borisey.personal_finance.models.Balance;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface BalanceRepository extends CrudRepository<Balance, Long> {
    Iterable<Balance> findAll(Sort colName);
    Iterable<Balance> findByUserId(Long userId, Sort colName);
}
