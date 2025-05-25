package com.pm.patientservice.controller;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.dto.validationGroup.CreatePatientValidationGroup;
import com.pm.patientservice.kafka.PatientKafkaProducer;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient" ,description="API for managing Patients")
public class PatientController {

    private final PatientService patientService;
    private final PatientKafkaProducer patientKafkaProducer;

    public PatientController(PatientService patientService,PatientKafkaProducer patientKafkaProducer){
        this.patientService=patientService;
        this.patientKafkaProducer = patientKafkaProducer;
    }

    @GetMapping
    @Operation(summary = "Get Patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients(){
        List<PatientResponseDTO> patientResponseDTOList= patientService.getPatients();
        return ResponseEntity.ok().body(patientResponseDTOList);
    }

    @PostMapping
    @Operation(summary = "Create a new Patient")
    public  ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO){

      PatientResponseDTO patientRes=patientService.createPatient(patientRequestDTO);

      return ResponseEntity.ok().body(patientRes);
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Update a Patient")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID uuid
                                                            ,@Validated({Default.class})
            @RequestBody PatientRequestDTO patientRequestDTO){

        PatientResponseDTO patientResponseDTO = patientService.updatePatient(uuid,patientRequestDTO);

        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @DeleteMapping("/{uuid}")
    @Operation(summary = " Delete a Patient")
    public ResponseEntity<Void> deletePatient( @PathVariable UUID uuid)
    {
        patientService.deletePatient(uuid);
        return ResponseEntity.noContent().build();
    }
}
