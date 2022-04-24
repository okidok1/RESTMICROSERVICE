package com.example.DimaKrutoi.consumer;

import com.example.DimaKrutoi.dto.TreeDto;
import com.example.DimaKrutoi.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    @Autowired
    private TreeService service;

    @KafkaListener(topics = "tree-msg-listener-topic", groupId = "kafkaListener-group")
    public void listen(TreeDto treeDto) {

        System.out.println("Received TreeDto information : " + treeDto);
        service.save(treeDto);
    }

}
