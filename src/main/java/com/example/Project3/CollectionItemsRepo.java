package com.example.Project3;

import com.example.Project3.projections.PoseSummary;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CollectionItemsRepo extends JpaRepository<com.example.Project3.entities.CollectionItems, Integer> {

    //get items in a collection with pose details
  @Query(value = """
      SELECT p.id   AS id,
             p.name AS name,
             p.style AS style,
             p.difficulty AS difficulty,
             ci.position AS position
      FROM collection_items ci
      JOIN poses p ON p.id = ci.pose_id
      WHERE ci.collection_id = :collectionId
      ORDER BY ci.position ASC
    """, nativeQuery = true)
  List<PoseSummary> findPoseSummaries(@Param("collectionId") Integer collectionId);

  //check for duplicate poses in a collection
  @Query("""
    select count(ci) > 0
    from CollectionItems ci
    where ci.collection.id = :collectionId and ci.pose.id = :poseId
  """)
  boolean existsInCollection(@Param("collectionId") Integer collectionId, @Param("poseId") Integer poseId);

// append item to the end of a collection + positioning
  @Query("""
    select coalesce(max(ci.position), 0)
    from CollectionItems ci
    where ci.collection.id = :collectionId
  """)
  Integer maxPosition(@Param("collectionId") Integer collectionId);
}

