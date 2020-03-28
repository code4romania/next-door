package com.code4ro.nextdoor.emergency.contact.controller;

import com.code4ro.nextdoor.core.exception.NextDoorValidationException;
import com.code4ro.nextdoor.emergency.contact.dto.EmergencyContactDto;
import com.code4ro.nextdoor.emergency.contact.service.EmergencyContactService;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emergency-contacts")
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;

    public EmergencyContactController(EmergencyContactService emergencyContactService) {
        this.emergencyContactService = emergencyContactService;
    }

    @PostMapping
    public ResponseEntity<EmergencyContactDto> save(@RequestBody EmergencyContactDto emergencyContactDto) {
        final EmergencyContactDto savedEmergencyContact =
            emergencyContactService.save(emergencyContactDto);

        return new ResponseEntity<>(savedEmergencyContact, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<EmergencyContactDto> update(@RequestBody EmergencyContactDto emergencyContactDto) {
        final EmergencyContactDto savedEmergencyContact =
            emergencyContactService.update(emergencyContactDto);

        return ResponseEntity.ok(savedEmergencyContact);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") String id) {
        emergencyContactService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<EmergencyContactDto> getById(@PathVariable("id") String id) {
        final EmergencyContactDto emergencyContactDto =
            emergencyContactService.findByUUID(id);

        if (Objects.isNull(emergencyContactDto)) {
            throw new NextDoorValidationException("id.not.found", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(emergencyContactDto);
    }

    @GetMapping
    public ResponseEntity<List<EmergencyContactDto>> getAll() {
        final List<EmergencyContactDto> emergencyContactDtoList =
            emergencyContactService.findAll();

        return ResponseEntity.ok(emergencyContactDtoList);
    }
}
