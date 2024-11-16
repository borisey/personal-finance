package com.borisey.personal_finance.repo;

import com.borisey.personal_finance.models.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.RequestParam;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Iterable<Category> findAll(Sort colName);

    @Query(value = "SELECT category.id, category.title, category.created, category.parent_id, category.type_id, category.updated, category.user_id, sum(t.amount) as allamount "
            + " FROM Category category "
            + " LEFT JOIN balance t ON (t.category_id=category.id) "
            + " WHERE category.user_id=?1 "
            + " AND category.type_id=?2 "
            + " AND (t.type_id=category.type_id OR t.type_id IS NULL) "
            + " GROUP BY category.id "
            , nativeQuery = true)
    Iterable<Category> findByUserIdAndTypeId(Long userId, Byte typeId, Sort colName);
}
