    package com.pm.patientservice.service;

    import com.pm.patientservice.dto.PatientRequestDTO;
    import com.pm.patientservice.dto.PatientResponseDTO;
    import com.pm.patientservice.exception.EmailAlreadyExistsException;
    import com.pm.patientservice.exception.PatientNotFoundException;
    import com.pm.patientservice.grpc.BillingServiceGrpcClient;
    import com.pm.patientservice.kafka.PatientKafkaProducer;
    import com.pm.patientservice.mapper.PatientMapper;
    import com.pm.patientservice.model.Patient;
    import com.pm.patientservice.repository.PatientRepository;
    import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Service;
    import org.springframework.web.bind.annotation.PostMapping;

    import java.time.LocalDate;
    import java.util.List;
    import java.util.UUID;

    @Service
    public class PatientService {

        private  final PatientRepository patientRepository;
        private final BillingServiceGrpcClient billingServiceGrpcClient;
        private final PatientKafkaProducer patientKafkaProducer;

        public PatientService(PatientRepository patientRepository
                ,BillingServiceGrpcClient billingServiceGrpcClient
                 ,PatientKafkaProducer patientKafkaProducer){
            this.patientRepository=patientRepository;
            this.billingServiceGrpcClient=billingServiceGrpcClient;
            this.patientKafkaProducer = patientKafkaProducer;
        }

        public List<PatientResponseDTO> getPatients(){
            List<Patient> patients =patientRepository.findAll();
           List<PatientResponseDTO> responseDTOS=patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();
           return  responseDTOS;
        }


        public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){
            Patient patient = PatientMapper.toModel(patientRequestDTO);

            if(patientRepository.existsByemail(patientRequestDTO.getEmail())){
                throw new EmailAlreadyExistsException(" A patient already exists with this email"+patientRequestDTO.getEmail());
            }

            Patient savedPatient = patientRepository.save(patient);



            // gRPC call
            billingServiceGrpcClient.createBillingAccount(savedPatient.getId().toString(),savedPatient.getName(),savedPatient.getEmail());

            // Send to Kafka topic
            patientKafkaProducer.sendPatientData(patientRequestDTO);
            return PatientMapper.toDTO(savedPatient);
        }

        public PatientResponseDTO updatePatient(UUID uuid,PatientRequestDTO requestDTO){
            Patient patient=patientRepository.findById(uuid)
                    .orElseThrow(()->new PatientNotFoundException("Patient not with ID:"+uuid));
            String emailToCheck = requestDTO.getEmail().trim().toLowerCase();
            if(patientRepository.existsByemail(emailToCheck) ){
                throw new EmailAlreadyExistsException(" A patient already exists with this email"+requestDTO.getEmail());
            }
            if(patientRepository.existsByEmailAndIdNot(requestDTO.getEmail(),uuid) ){
                throw new EmailAlreadyExistsException(" A patient already exists with this email"+requestDTO.getEmail()+" and "+uuid);
            }


            patient.setName(requestDTO.getName());
            patient.setEmail(requestDTO.getEmail());
            patient.setAddress(requestDTO.getAddress());
            patient.setDateOfBirth(LocalDate.parse(requestDTO.getDateOfBirth()));

            Patient updatedPatient=patientRepository.save(patient);

            return PatientMapper.toDTO(updatedPatient);
        }
        public void deletePatient(UUID id){
            patientRepository.deleteById(id);
        }
    }
