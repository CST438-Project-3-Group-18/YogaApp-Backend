package com.example.Project3;

import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project3.entities.Poses;

@RestController
public class PosesController {
    private final PosesRepository posesRepo;
    private final Random random = new Random();

    public PosesController(PosesRepository posesRepo){
        this.posesRepo = posesRepo;
    }

    @GetMapping("/poses")
    public List<Poses> all(){
        return posesRepo.findAll();
    }

    //get random poses
    @GetMapping("/poses/random")
    public Poses randomPoses(){
        List<Poses> allPoses = posesRepo.findAll();
        int randomIndex = random.nextInt(allPoses.size());
        return allPoses.get(randomIndex);
    }
}