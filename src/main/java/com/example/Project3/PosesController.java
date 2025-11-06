package com.example.Project3;

import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project3.entities.Poses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/poses") 
public class PosesController {
    private final PosesRepository posesRepo;
    private final Random random = new Random();

    public PosesController(PosesRepository posesRepo){
        this.posesRepo = posesRepo;
    }

    @GetMapping
    public List<Poses> all(){
        return posesRepo.findAll();
    }

    //get random poses
    @GetMapping("/random")
    public Poses randomPoses(){
        List<Poses> allPoses = posesRepo.findAll();
        int randomIndex = random.nextInt(allPoses.size());
        return allPoses.get(randomIndex);
    }
    
    // style filtering 
    // i want this route to be /poses/{style}
    @GetMapping("/style/{style}") 
    public List<Poses> byStyle(@PathVariable String style){
        return posesRepo.findByStyle(style);
    }


    // difficulty 
    @GetMapping("/difficulty/{difficulty}")
    public List<Poses> byDifficulty(@PathVariable String difficulty){
        return posesRepo.findByDifficultyIgnoreCase(difficulty);
    }


    // search by keyword in description 
    }