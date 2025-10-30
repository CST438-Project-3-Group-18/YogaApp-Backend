package com.example.Project3.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
//@Table(name = "collections")
public class CollectionsE {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int userId;
  private String name;

  public CollectionsE() {
  }
  public CollectionsE(int collectionId, String name) {
    this.userId = collectionId;
    this.name = name;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setUserId(int collectionId) {
    this.userId = collectionId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public int getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }
}
