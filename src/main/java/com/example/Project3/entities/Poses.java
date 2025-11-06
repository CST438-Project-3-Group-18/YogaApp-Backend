package com.example.Project3.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "poses")
public class Poses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;
    private String image;       
    private String difficulty;  
    private String style; 

    public Poses() {
    }

    public Poses(String name, String description, String image, String difficulty, String style) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.difficulty = difficulty;
        this.style = style;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getImage() { 
        return image; }

    public void setImage(String image) { 
        this.image = image; }

    public String getDifficulty() { 
        return difficulty; }

    public void setDifficulty(String difficulty) { 
        this.difficulty = difficulty; }

    public String getStyle() { 
        return style; }

    public void setStyle(String style) { 
        this.style = style; }
}
