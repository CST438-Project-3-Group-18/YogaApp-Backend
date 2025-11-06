package com.example.Project3;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Project3.entities.Poses;

public interface PosesRepository extends JpaRepository<Poses,Integer > {

    List<Poses> findByStyle(String style);
  
}
