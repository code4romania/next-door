package com.code4ro.nextdoor.emergency.contact.controller;

import com.code4ro.nextdoor.core.exception.NextDoorValidationException;
import com.code4ro.nextdoor.emergency.contact.dto.EmergencyContactDto;
import com.code4ro.nextdoor.emergency.contact.service.EmergencyContactService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(value = "Emergency Contact CRUD Controller")
@RequestMapping("/api/emergency-contacts")
public class EmergencyContactController {

    private final EmergencyContactService emergencyContactService;

    @Autowired
    public EmergencyContactController(EmergencyContactService emergencyContactService) {
        this.emergencyContactService = emergencyContactService;
    }

    @PostMapping
    @ApiOperation(value = "Saves an Emergency Contact")
    public ResponseEntity<EmergencyContactDto> save(@RequestBody EmergencyContactDto emergencyContactDto) {
        final EmergencyContactDto savedEmergencyContact =
            emergencyContactService.save(emergencyContactDto);

        return new ResponseEntity<>(savedEmergencyContact, HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation(value = "Updates an Emergency Contact")
    public ResponseEntity<EmergencyContactDto> update(@RequestBody EmergencyContactDto emergencyContactDto) {
        final EmergencyContactDto savedEmergencyContact =
            emergencyContactService.update(emergencyContactDto);

        return ResponseEntity.ok(savedEmergencyContact);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes an Emergency Contact by id")
    public ResponseEntity<Void> deleteById(@PathVariable("id") String id) {
        emergencyContactService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Gets an Emergency Contact by id")
    public ResponseEntity<EmergencyContactDto> getById(@PathVariable("id") String id) {
        Optional<EmergencyContactDto> emergencyContactDto =
            emergencyContactService.findByUUID(id);

        return emergencyContactDto.map(ResponseEntity::ok)
            .orElseThrow(() -> new NextDoorValidationException("id.not.found", HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @ApiOperation(value = "Gets all Emergency Contacts")
    public ResponseEntity<List<EmergencyContactDto>> getAll() {
        final List<EmergencyContactDto> emergencyContactDtoList =
            emergencyContactService.findAll();

        return ResponseEntity.ok(emergencyContactDtoList);
    }
}
