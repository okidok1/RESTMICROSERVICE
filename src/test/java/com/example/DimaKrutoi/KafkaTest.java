package com.example.DimaKrutoi;

import com.example.DimaKrutoi.config.ContainersEnvironment;
import com.example.DimaKrutoi.dto.TreeDto;
import com.example.DimaKrutoi.entity.Tree;
import com.example.DimaKrutoi.repository.TreeRepository;
import com.example.DimaKrutoi.service.KafkaService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.util.Collections.singletonList;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DimaKrutoiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KafkaTest extends ContainersEnvironment {

    @Autowired
    private KafkaService kafkaService;
    @Autowired
    private TreeRepository treeRepository;
    @Autowired
    private KafkaConsumerConfig kafkaConsumerConfig;

    @LocalServerPort
    int RandomPort;

    String url = "http://localhost:";

    TestRestTemplate testRestTemplate = new TestRestTemplate();

    @BeforeEach
    public void resetDb() {
        treeRepository.deleteAll();
        List<Tree> treeList = treeRepository.findAll();
        String isDbClean = (treeList.isEmpty()) ? "clean" : "notClean";
        System.out.println("Log: cleaning db ---> " + isDbClean);
        assertEquals(0, treeList.size());
    }

    @Test
    public void testUsageKafka(){
        TreeDto treeDto = new TreeDto();
        treeDto.setHeight(3);
        treeDto.setName("kafkaTest");
        kafkaService.sendForListenerTopic(treeDto);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Tree> treeList = treeRepository.findAll();
        assertEquals(1, treeList.size());

        Tree tree = treeList.stream().findAny().get();

        assertEquals(treeDto.getHeight(), tree.getHeight());
        assertEquals(treeDto.getName(),tree.getName());
    }

    @Test
    void msgInTopicTest() {

        Tree updateInfoTree = new Tree()
                .setName("springBootTestTree")
                .setHeight(21);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Tree> requestBody = new HttpEntity<>(updateInfoTree, headers);

        Consumer<String, TreeDto> consumer = kafkaConsumerConfig.consumerFactory().createConsumer("kafkaListener-group",null);
        String topicName = "tree-msg-check-topic";
        consumer.subscribe(singletonList(topicName));

        testRestTemplate.exchange(url + RandomPort + "/trees", HttpMethod.POST, requestBody, Void.class);


        List<ConsumerRecord<String, TreeDto>> allRecords = new ArrayList<>();

        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
            ConsumerRecords<String, TreeDto> records = consumer.poll(Duration.ofMillis(100));

            if (records.isEmpty()) {
                return false;
            }
            records.iterator().forEachRemaining(allRecords::add);

            TreeDto treeDto = allRecords.stream().findFirst().get().value();

            assertEquals(updateInfoTree.getHeight(), treeDto.getHeight());
            assertEquals(updateInfoTree.getName(),treeDto.getName());

            return true;
        });

        consumer.unsubscribe();

    }

}
