package com.Learning.userRepo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Learning.Entity.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}