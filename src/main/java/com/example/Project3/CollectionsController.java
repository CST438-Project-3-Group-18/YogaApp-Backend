package com.example.Project3;

import com.example.Project3.entities.CollectionItems;
import com.example.Project3.entities.Collections;
import com.example.Project3.entities.Poses;
import com.example.Project3.projections.PoseSummary;
import java.net.URI;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

@RestController
@RequestMapping("/collections")
@CrossOrigin(origins = { "http://localhost:8081", "http://127.0.0.1:8081" })
public class CollectionsController {
  private final CollectionsRepo collectionsRepo;
  private final CollectionItemsRepo collectionItemsRepo;
  private final PosesRepository posesRepository;

  public CollectionsController(CollectionsRepo collectionsRepo,
                               CollectionItemsRepo collectionItemsRepo,
                               PosesRepository posesRepository) {
    this.collectionsRepo = collectionsRepo;
    this.collectionItemsRepo = collectionItemsRepo;
    this.posesRepository = posesRepository;
  }

  //list all
  @GetMapping //("/collections")
  public List<Collections> listAll(){
    return collectionsRepo.findAll();
  }

  //collection by userId
  @GetMapping("/user/{userId}")
  public List<Collections> getCollectionsByUser(@PathVariable int userId) {
    return collectionsRepo.findByUserId(userId);
  }

  //collection by id (primary key)
  @GetMapping("/{id}")
  ResponseEntity<Collections> getCollection(@PathVariable("id") Integer id) {
    return collectionsRepo.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<?> create(@RequestBody Collections body) {
    try{
      if(body.getName() == null || body.getName().isBlank()){
        return ResponseEntity.badRequest().body("Name is required");
      }
      Collections saved = collectionsRepo.save(body);
      return ResponseEntity
          .created(URI.create("/collections/" + saved.getId()))
          .body(saved);
    } catch (DataIntegrityViolationException e){
      return ResponseEntity.unprocessableEntity().body("Invalid data: " + e.getMostSpecificCause().getMessage());
    }
}
  // Deletes an item by its id
  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Integer id) {
    if (!collectionsRepo.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    try {
      collectionsRepo.deleteById(id);
      return ResponseEntity.noContent().build(); // 204
    } catch (DataIntegrityViolationException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Cannot delete collection: it is referenced by other records.");
    }
  }

}
