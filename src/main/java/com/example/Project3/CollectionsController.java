package com.example.Project3;

import com.example.Project3.entities.Collections;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collections")
public class CollectionsController {
  private final CollectionsRepo collectionsRepo;

  public CollectionsController(CollectionsRepo collectionsRepo) {
    this.collectionsRepo = collectionsRepo;
  }

//  @GetMapping ("/collections")
  List<Collections> getCollections(){
    return collectionsRepo.findAll();
  }
  @GetMapping("/{id}")
  ResponseEntity<Collections> getCollection(@PathVariable Integer id) {
    return collectionsRepo.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

}
