package com.example.Project3.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "collections")
public class Collections {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "user_id", nullable = false)

  @JsonProperty("user_id")
  private int userId;
  @Column(nullable= false)
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
