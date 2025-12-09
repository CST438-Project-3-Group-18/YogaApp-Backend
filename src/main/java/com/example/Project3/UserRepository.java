package com.example.Project3;

import com.example.Project3.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String name);
    Optional<User> findByName(String name);
    Optional<User> findByNameAndPassword(String name, String password);
}
