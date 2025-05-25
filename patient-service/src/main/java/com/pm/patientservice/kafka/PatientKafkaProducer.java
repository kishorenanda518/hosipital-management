package com.pm.patientservice.kafka;

import com.pm.patientservice.dto.PatientRequestDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PatientKafkaProducer {
    private static final String TOPIC ="patient";

    
    private final KafkaTemplate<String, PatientRequestDTO> kafkaTemplate;

    public PatientKafkaProducer(KafkaTemplate<String,PatientRequestDTO> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }
    public  void sendPatientData(PatientRequestDTO patientRequestDTO){
        kafkaTemplate.send(TOPIC,patientRequestDTO);
    }
}
