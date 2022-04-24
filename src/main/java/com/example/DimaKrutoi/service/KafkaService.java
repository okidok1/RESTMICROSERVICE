package com.example.DimaKrutoi.service;


import com.example.DimaKrutoi.dto.TreeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    private final Logger LOG = LoggerFactory.getLogger(KafkaService.class);
    @Autowired
    private KafkaTemplate<String, TreeDto> kafkaTemplate;

    String kafkaTopic = "tree-msg-listener-topic";
    String kafkaTopicCheck = "tree-msg-check-topic";

    public void sendForListenerTopic(TreeDto treeDto) {
        LOG.info("Sending Tree Json Serializer : {}", treeDto);
        kafkaTemplate.send(kafkaTopic, treeDto);
    }

    public void sendForCheckTopic(TreeDto treeDto) {
        LOG.info("Sending Tree Json Serializer : {}", treeDto);
        kafkaTemplate.send(kafkaTopicCheck, treeDto);
    }
}
