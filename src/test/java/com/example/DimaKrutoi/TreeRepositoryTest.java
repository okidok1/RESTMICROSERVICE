package com.example.DimaKrutoi;

import com.example.DimaKrutoi.config.ContainersEnvironment;
import com.example.DimaKrutoi.entity.Tree;
import com.example.DimaKrutoi.repository.TreeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DimaKrutoiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class TreeRepositoryTest extends ContainersEnvironment {

    @Autowired
    private TreeRepository treeRepository;

    @LocalServerPort
    int RandomPort;

    String url = "http://localhost:";

    TestRestTemplate testRestTemplate = new TestRestTemplate();

    @Test
    void whenGetAll() {

        Tree updateInfoTree = new Tree()
                .setName("springBootTestTree")
                .setHeight(21)
                .setId("springBootTestTreeId")
                .setCreationTS(LocalDateTime.now());

        treeRepository.save(updateInfoTree);
        List<Tree> treeList = treeRepository.findAll();

        ResponseEntity<String> response = testRestTemplate.getForEntity(url + RandomPort + "/trees", String.class);
        System.out.println("Log: responseCheck --- " + response.getStatusCode());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(1, treeList.size());

    }

    @Test
    void whenSave() {

        Tree updateInfoTree = new Tree()
                .setName("springBootTestTree")
                .setHeight(21);

        ResponseEntity<String> responseBeforeSave = testRestTemplate.getForEntity(url + RandomPort + "/trees", String.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Tree> requestBody = new HttpEntity<>(updateInfoTree, headers);

        testRestTemplate.exchange(url + RandomPort + "/trees", HttpMethod.POST, requestBody, Void.class);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ResponseEntity<String> responseAfterSave = testRestTemplate.getForEntity(url + RandomPort + "/trees", String.class);

        System.out.println("Log: status check ---> " + responseAfterSave.getStatusCode());
        assertEquals(HttpStatus.OK, responseAfterSave.getStatusCode());

        String equalityOfResponses = (responseAfterSave.getBody().equals(responseBeforeSave.getBody())) ? "equal" : "notEqual";
        System.out.println("Log: making sure that responses are not equal ---> " + equalityOfResponses);
        assertFalse(responseAfterSave.getBody().equals(responseBeforeSave.getBody()));

    }

    @BeforeEach
    public void resetDb() {
        treeRepository.deleteAll();
        List<Tree> treeList = treeRepository.findAll();
        String isDbClean = (treeList.isEmpty()) ? "clean" : "notClean";
        System.out.println("Log: cleaning db ---> " + isDbClean);
        assertEquals(0, treeList.size());
    }

}