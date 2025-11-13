package com.example.Project3;

import com.example.Project3.entities.Collections;
import java.net.URI;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/collections")
@CrossOrigin(origins = { "http://localhost:8081", "http://127.0.0.1:8081" })
public class CollectionsController {
  private final CollectionsRepo collectionsRepo;

  public CollectionsController(CollectionsRepo collectionsRepo) {
    this.collectionsRepo = collectionsRepo;
  }

  //list all
  @GetMapping //("/collections")
  public List<Collections> listAll(){
    return collectionsRepo.findAll();
  }

  //collection by userId
  public List<Collections> getCollectionsByUser(@RequestParam int userId) {
    return collectionsRepo.findByUserId(userId);
  }

  //collection by id
  @GetMapping("/{id}")
  ResponseEntity<Collections> getCollection(@PathVariable("id") Integer id) {
    return collectionsRepo.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Collections> createCollection(@RequestBody Collections data) {
    data.setId(null);
    Collections saved = collectionsRepo.save(data);
    return ResponseEntity
        .created(URI.create("/collections/" + saved.getId()))
        .body(saved);
  }
  @DeleteMapping("/{id}")
  public ResponseEntity<Collections> deleteCollection(@PathVariable("id") Integer id) {
    if(!collectionsRepo.existsById(id)){
      return ResponseEntity.notFound().build();
    }
    try{
      collectionsRepo.deleteById(id);
      return ResponseEntity.noContent().build();
    } catch (DataIntegrityViolationException e){
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
  }
}
