package com.malinouski.cache.user.repo;

import com.malinouski.cache.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
