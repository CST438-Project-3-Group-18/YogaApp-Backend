package com.example.Project3;

import com.example.Project3.entities.Collections;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CollectionsRepo extends JpaRepository<Collections, Integer> {
    List<Collections> findByUserId(int userId);
}

