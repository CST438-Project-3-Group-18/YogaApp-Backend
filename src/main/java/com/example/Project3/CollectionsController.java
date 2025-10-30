package com.example.Project3;

import com.example.Project3.entities.CollectionsE;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectionsController {
  private final CollectionsRepo collectionsRepo;
  public CollectionsController(CollectionsRepo collectionsRepo) {
    this.collectionsRepo = collectionsRepo;
  }

  @GetMapping ("/collections")
  List<CollectionsE> getCollections(){
    return collectionsRepo.findAll();
  }
}
