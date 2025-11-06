package com.example.Project3.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
//@Table(name = "collections")
public class Collections {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int userId;
  private String name;

  public Collections() {
  }
  public Collections(int collectionId, String name) {
    this.userId = collectionId;
    this.name = name;
  }

  public void setId(Integer id) {
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
