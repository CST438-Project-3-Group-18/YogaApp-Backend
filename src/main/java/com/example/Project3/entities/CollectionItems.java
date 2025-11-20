package com.example.Project3.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "collection_items")
public class CollectionItems {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "collection_id")
  private Collections collection;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pose_id")
  private Poses pose;

  @Column(nullable = false)
  private Integer position; // order inside the collection

  public Integer getId() { return id; }
  public Collections getCollection() { return collection; }
  public Poses getPose() { return pose; }
  public Integer getPosition() { return position; }

  public void setId(Integer id) { this.id = id; }
  public void setCollection(Collections collection) { this.collection = collection; }
  public void setPose(Poses pose) { this.pose = pose; }
  public void setPosition(Integer position) { this.position = position; }
}
