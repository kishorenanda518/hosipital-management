package com.pm.analyticservice.kafka;

import com.pm.analyticservice.dto.PatientRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytic-group")
    public void consume(PatientRequestDTO patient) {
        System.out.println("Received from Kafka: " + patient.getName()+","+patient.getEmail());
    }
}
