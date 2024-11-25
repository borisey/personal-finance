package com.borisey.personal_finance.repo;

import com.borisey.personal_finance.models.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDateTime;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Iterable<Category> findAll(Sort colName);

    @Query(value = "SELECT category.id, category.budget, category.title, category.created, category.parent_id, category.type_id, category.updated, category.user_id, sum(balance.amount) as allamount "
            + " FROM category category "
            + " LEFT JOIN balance balance ON (balance.category_id=category.id) "
            + " WHERE category.user_id=?1 "
            + " AND category.type_id=?2 "
            + " AND (balance.type_id=category.type_id OR balance.type_id IS NULL) "
            + " AND balance.date>=?3 "
            + " AND balance.date<=?4 "
            + " GROUP BY category.id "
            , nativeQuery = true)
    Iterable<Category> findByUserIdAndTypeIdAmount(Long userId, Byte typeId, LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo, Sort colName);

//    @Query(value = "SELECT b FROM Balance b WHERE b.userId = ?1 AND b.typeId = ?2", nativeQuery = true)
    Iterable<Category> findByUserIdAndTypeId(Long userId, Byte typeId, Sort colName);
}
