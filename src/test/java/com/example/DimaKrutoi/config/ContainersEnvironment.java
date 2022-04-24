package com.example.DimaKrutoi.config;

import com.example.DimaKrutoi.containers.KafkaTestContainer;
import com.example.DimaKrutoi.containers.PostgresTestContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();

    @Container
    public static KafkaContainer kafkaContainer = KafkaTestContainer.getInstance();
}
