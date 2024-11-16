package com.borisey.personal_finance.repo;

import com.borisey.personal_finance.models.Balance;
import com.borisey.personal_finance.models.Type;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface BalanceRepository extends CrudRepository<Balance, Long> {
    Iterable<Balance> findAll(Sort colName);
    Iterable<Balance> findByUserId(Long userId, Sort colName);
    @Query(value = "SELECT sum(amount) FROM Balance a WHERE a.userId = ?1 AND a.type = ?2")
    Iterable<Balance> findSumByUserIdTypeId(Long userId, Type type);
}
