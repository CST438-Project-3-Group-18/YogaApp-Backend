package com.example.Project3;

import com.example.Project3.projections.PoseSummary;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CollectionItemsRepo extends JpaRepository<com.example.Project3.entities.CollectionItems, Integer> {

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
}
