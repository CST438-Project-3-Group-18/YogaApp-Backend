package com.example.Project3;

import com.example.Project3.entities.CollectionItems;
import com.example.Project3.entities.Collections;
import com.example.Project3.entities.Poses;
import com.example.Project3.projections.PoseSummary;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  // list all collections
  @GetMapping
  public List<Collections> listAll() {
    return collectionsRepo.findAll();
  }

  // collections by userId
  @GetMapping("/user/{userId}")
  public List<Collections> getCollectionsByUser(@PathVariable int userId) {
    return collectionsRepo.findByUserId(userId);
  }

  // collection by id (primary key)
  @GetMapping("/{id}")
  public ResponseEntity<Collections> getCollection(@PathVariable("id") Integer id) {
    return collectionsRepo.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

// body we expect when adding a pose to a collection
public record AddItemRequest(Integer poseId) {}

@PostMapping("/{id}/items")
public ResponseEntity<?> addItemToCollection(
    @PathVariable("id") Integer id,
    @RequestBody AddItemRequest body
) {
  // 1) validate poseId exists in body
  if (body.poseId() == null) {
    return ResponseEntity.badRequest().body("poseId is required");
  }

  // 2) make sure the collection exists
  Optional<Collections> collectionOpt = collectionsRepo.findById(id);
  if (collectionOpt.isEmpty()) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Collection not found");
  }

  // 3) make sure the pose exists
  Optional<Poses> poseOpt = posesRepository.findById(body.poseId());
  if (poseOpt.isEmpty()) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pose not found");
  }

  // 4) create the CollectionItems row (matches your entity)
  CollectionItems item = new CollectionItems();
  item.setCollection(collectionOpt.get());   // << uses @ManyToOne Collections collection;
  item.setPose(poseOpt.get());              // << uses @ManyToOne Poses pose;
  item.setPosition(0);                      // << required: position is @Column(nullable = false)

  CollectionItems saved = collectionItemsRepo.save(item);

  return ResponseEntity
      .status(HttpStatus.CREATED)
      .body(saved);
}

  // create new collection
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
               produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> create(@RequestBody Collections body) {
    try {
      if (body.getName() == null || body.getName().isBlank()) {
        return ResponseEntity.badRequest().body("Name is required");
      }
      Collections saved = collectionsRepo.save(body);
      return ResponseEntity
          .created(URI.create("/collections/" + saved.getId()))
          .body(saved);
    } catch (DataIntegrityViolationException e) {
      return ResponseEntity.unprocessableEntity()
          .body("Invalid data: " + e.getMostSpecificCause().getMessage());
    }
  }

  // delete collection by id
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

  // get items in a collection with pose details
  @GetMapping("/{id}/items")
  public List<PoseSummary> getItemsForCollection(@PathVariable("id") Integer id) {
    return collectionItemsRepo.findPoseSummaries(id);
  }
}
