package com.borisey.personal_finance.repo;

import com.borisey.personal_finance.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUUID(String UUID);
}
