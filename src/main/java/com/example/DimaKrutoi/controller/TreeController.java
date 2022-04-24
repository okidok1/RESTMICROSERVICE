package com.example.DimaKrutoi.controller;

import com.example.DimaKrutoi.dto.TreeDto;
import com.example.DimaKrutoi.entity.Tree;
import com.example.DimaKrutoi.service.KafkaService;
import com.example.DimaKrutoi.service.TreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trees")
@RequiredArgsConstructor
public class TreeController {

    private final TreeService service;

    @Autowired
    KafkaService kafkaService;

    @PostMapping
    public Tree save(@RequestBody TreeDto tree){
        kafkaService.sendForCheckTopic(tree);

        return service.save(tree);
    }

    @GetMapping Iterable<Tree> getAll(){
        return service.getAll();
    }


}
