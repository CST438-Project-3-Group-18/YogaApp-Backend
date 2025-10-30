package com.example.Project3;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project3.entities.Poses;

@RestController
public class PosesController {
    private final PosesRepository posesRepo;

    public PosesController(PosesRepository posesRepo){
        this.posesRepo = posesRepo;
    }

    @GetMapping("/poses")
    public List<Poses> all(){
        return posesRepo.findAll();
    }
    
}
