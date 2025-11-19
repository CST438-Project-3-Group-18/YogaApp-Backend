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
  @GetMapping(params = "userId")
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

  //get collection items
@GetMapping("/{id}/items")
  public ResponseEntity<List<PoseSummary>> listItems(@PathVariable Integer id) {
    if (!collectionsRepo.existsById(id)) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(collectionItemsRepo.findPoseSummaries(id));
  }

  //add an item
  public static record AddItemRequest(Integer poseId, Integer position) {}

  @PostMapping("/{id}/items")
  public ResponseEntity<?> addItem(@PathVariable Integer id, @RequestBody AddItemRequest body) {
    if (body == null || body.poseId() == null) {
      return ResponseEntity.badRequest().body("poseId is required");
    }

    Optional<Collections> colOpt = collectionsRepo.findById(id);
    if (colOpt.isEmpty()) return ResponseEntity.notFound().build();

    Optional<Poses> poseOpt = posesRepository.findById(body.poseId());
    if (poseOpt.isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pose not found");

    // Duplicate guard
    if (collectionItemsRepo.existsInCollection(id, body.poseId())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Pose already in this collection");
    }
 int next = collectionItemsRepo.maxPosition(id);
    int position = body.position() != null ? body.position() : next + 1;

    CollectionItems ci = new CollectionItems();
    ci.setCollection(colOpt.get());
    ci.setPose(poseOpt.get());
    ci.setPosition(position);

    CollectionItems saved = collectionItemsRepo.save(ci);

    return ResponseEntity
      .created(URI.create("/collections/" + id + "/items/" + saved.getId()))
      .build();
  }
}
