package com.example.DimaKrutoi.containers;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public class KafkaTestContainer extends KafkaContainer {

    public static final DockerImageName KAFKA_TEST_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:7.0.1");
    public static KafkaTestContainer container;

    public KafkaTestContainer() {
        super(KAFKA_TEST_IMAGE);
    }

    public static KafkaTestContainer getInstance(){
        if(container == null){
            container = new KafkaTestContainer();
        }
        return container;
    }

    @Override
    public void start(){
        super.start();
        System.setProperty("KAFKA_URL", container.getHost() + ':' + container.getFirstMappedPort());
    }

}
