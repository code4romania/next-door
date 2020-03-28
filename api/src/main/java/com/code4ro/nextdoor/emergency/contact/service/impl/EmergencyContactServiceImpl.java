package com.code4ro.nextdoor.emergency.contact.service.impl;

import com.code4ro.nextdoor.emergency.contact.dto.EmergencyContactDto;
import com.code4ro.nextdoor.emergency.contact.entity.EmergencyContact;
import com.code4ro.nextdoor.emergency.contact.repository.EmergencyContactRepository;
import com.code4ro.nextdoor.emergency.contact.service.EmergencyContactService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class EmergencyContactServiceImpl implements EmergencyContactService {

    private final EmergencyContactRepository emergencyContactRepository;

    public EmergencyContactServiceImpl(EmergencyContactRepository emergencyContactRepository) {
        this.emergencyContactRepository = emergencyContactRepository;
    }

    @Override
    public EmergencyContactDto save(EmergencyContactDto emergencyContactDto) {
        EmergencyContact emergencyContact = mapDtoToEntity(emergencyContactDto);
        EmergencyContact emergencyContactDB = emergencyContactRepository.save(emergencyContact);

        return mapDtoToDTO(emergencyContactDB);
    }

    @Override
    public EmergencyContactDto update(EmergencyContactDto emergencyContactDto) {
        Optional<EmergencyContact> dbEntity = emergencyContactRepository.findById(
            UUID.fromString(emergencyContactDto.getId()));

        if (dbEntity.isPresent()) {
            EmergencyContact dbEmergencyContact = dbEntity.get();
            dbEmergencyContact.setAddress(emergencyContactDto.getAddress());
            dbEmergencyContact.setEmail(emergencyContactDto.getEmail());
            dbEmergencyContact.setName(emergencyContactDto.getName());
            dbEmergencyContact.setSurname(emergencyContactDto.getSurname());
            dbEmergencyContact.setTelephoneNumber(emergencyContactDto.getTelephoneNumber());
            return mapDtoToDTO(emergencyContactRepository.save(dbEmergencyContact));
        } else {
            return save(emergencyContactDto);
        }
    }

    @Override
    public void deleteById(String id) {
        Optional<EmergencyContact> dbEntity = emergencyContactRepository.findById(
            UUID.fromString(id));

        if (dbEntity.isPresent()) {
            emergencyContactRepository.deleteById(UUID.fromString(id));
        }
    }

    @Override
    public EmergencyContactDto findByUUID(String id) {
        return emergencyContactRepository.findById(UUID.fromString(id))
            .map(this::mapDtoToDTO)
            .orElse(null);
    }

    @Override
    public List<EmergencyContactDto> findAll() {
        return emergencyContactRepository.findAll()
            .stream()
            .map(this::mapDtoToDTO)
            .collect(Collectors.toList());
    }

    private EmergencyContact mapDtoToEntity(EmergencyContactDto emergencyContactDto) {
        return EmergencyContact.builder()
            .name(emergencyContactDto.getName())
            .surname(emergencyContactDto.getSurname())
            .address(emergencyContactDto.getAddress())
            .email(emergencyContactDto.getEmail())
            .telephoneNumber(emergencyContactDto.getTelephoneNumber())
            .build();
    }

    private EmergencyContactDto mapDtoToDTO(EmergencyContact emergencyContact) {
        return EmergencyContactDto.builder()
            .id(String.valueOf(emergencyContact.getId()))
            .name(emergencyContact.getName())
            .surname(emergencyContact.getSurname())
            .address(emergencyContact.getAddress())
            .email(emergencyContact.getEmail())
            .telephoneNumber(emergencyContact.getTelephoneNumber())
            .build();
    }
}
