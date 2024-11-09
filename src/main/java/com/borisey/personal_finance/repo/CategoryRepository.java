package com.borisey.personal_finance.repo;

import com.borisey.personal_finance.models.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

}
