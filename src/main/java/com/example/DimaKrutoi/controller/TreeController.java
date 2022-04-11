package com.example.DimaKrutoi.controller;

import com.example.DimaKrutoi.dto.TreeDto;
import com.example.DimaKrutoi.entity.Tree;
import com.example.DimaKrutoi.service.TreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trees")
@RequiredArgsConstructor
public class TreeController {

    private final TreeService service;

    @PostMapping
    public Tree save(@RequestBody TreeDto tree){
        return service.save(tree);
    }

    @GetMapping Iterable<Tree> getAll(){
        return service.getAll();
    }


}
