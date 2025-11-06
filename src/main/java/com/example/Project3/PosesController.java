package com.example.Project3;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project3.entities.Poses;

@RestController
@RequestMapping("/poses")
public class PosesController {
    private final PosesRepository posesRepo;

    public PosesController(PosesRepository posesRepo){
        this.posesRepo = posesRepo;
    }
    // ok to get all poses 
    // i want to change this to be /poses/all
    @GetMapping("/all")
    public List<Poses> all(){
        return posesRepo.findAll();
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
