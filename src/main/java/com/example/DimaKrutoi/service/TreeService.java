package com.example.DimaKrutoi.service;

import com.example.DimaKrutoi.dto.TreeDto;
import com.example.DimaKrutoi.entity.Tree;
import com.example.DimaKrutoi.repository.TreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TreeService {

    private final TreeRepository treeRepository;

    public Tree save(TreeDto treeDto){
        Tree tree = new Tree()
                .setName(treeDto.getName())
                .setHeight(treeDto.getHeight())
                .setId(UUID.randomUUID().toString())
//        I am informed that is better to use plugin for creating uid in DB, but in sake of completing task we using this method
                .setCreationTS(LocalDateTime.now());

        return treeRepository.save(tree);
    }

//better to use "pageable" here
    public Iterable<Tree> getAll(){
        return treeRepository.findAll();
    }
}
