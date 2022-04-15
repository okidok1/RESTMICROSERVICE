package com.example.DimaKrutoi;

import com.example.DimaKrutoi.config.ContainersEnvironment;
import com.example.DimaKrutoi.entity.Tree;
import com.example.DimaKrutoi.repository.TreeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DimaKrutoiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class TreeRepositoryTest extends ContainersEnvironment {

    @Autowired
    private TreeRepository treeRepository;

    @Test
    void contextLoads() {
        List<Tree> treeList = treeRepository.findAll();
        assertEquals(5, treeList.size());
        System.out.println("Test's done");
    }
}