package com.example.DimaKrutoi.controller;

import com.example.DimaKrutoi.dto.RandomAggregationDto;
import com.example.DimaKrutoi.dto.TreeDto;
import com.example.DimaKrutoi.entity.Tree;
import com.example.DimaKrutoi.service.KafkaService;
import com.example.DimaKrutoi.service.RandomOrgIntegrationService;
import com.example.DimaKrutoi.service.TreeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trees")
@RequiredArgsConstructor
public class TreeController {

    private final TreeService treeService;

    @Autowired
    KafkaService kafkaService;
    @Autowired
    RandomOrgIntegrationService randomService;

    @PostMapping
    public Tree save(@RequestBody TreeDto tree){
        kafkaService.sendForCheckTopic(tree);

        return treeService.save(tree);
    }

    @GetMapping Iterable<Tree> getAll(){
        return treeService.getAll();
    }

    @GetMapping("/random")
    public String getRandomNumber(RandomAggregationDto dto){
        @NonNull
        String answer = randomService.getRandomNumber(dto);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return answer;
    }

}
