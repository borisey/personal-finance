package com.borisey.personal_finance.repo;

import com.borisey.personal_finance.models.Balance;
import com.borisey.personal_finance.models.Type;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface BalanceRepository extends CrudRepository<Balance, Long> {

    Iterable<Balance> findAll(Sort colName);

    Iterable<Balance> findByUserId(Long userId, Sort colName);

    @Query(value = "SELECT sum(amount) FROM Balance b WHERE b.userId = ?1 AND b.type = ?2")
    Iterable<Balance> findSumByUserIdTypeId(Long userId, Type type);

//    @Query(value = "SELECT sum(amount) "
//            + " FROM Balance a "
//            + " WHERE a.userId = ?1 "
//            + " AND a.type = ?2 "
////            + " AND a.date >= ?3 "
////            + " AND a.date <= ?4 "
//            , nativeQuery = true)

    @Query(value = "SELECT sum(amount) FROM Balance b WHERE b.userId = ?1 AND b.type = ?2 AND b.date >= ?3 AND b.date <= ?4")
    Iterable<Balance> findSumByUserIdTypeIdDateTimeFromDateTimeTo(Long userId, Type type, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo);
}
