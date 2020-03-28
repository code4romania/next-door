package com.code4ro.nextdoor.emergency.contact.service.impl;

import com.code4ro.nextdoor.emergency.contact.dto.EmergencyContactDto;
import com.code4ro.nextdoor.emergency.contact.entity.EmergencyContact;
import com.code4ro.nextdoor.emergency.contact.repository.EmergencyContactRepository;
import com.code4ro.nextdoor.emergency.contact.service.EmergencyContactService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmergencyContactServiceImpl implements EmergencyContactService {

    private final EmergencyContactRepository emergencyContactRepository;

    @Autowired
    public EmergencyContactServiceImpl(EmergencyContactRepository emergencyContactRepository) {
        this.emergencyContactRepository = emergencyContactRepository;
    }

    @Override
    public EmergencyContactDto save(EmergencyContactDto emergencyContactDto) {
        EmergencyContact emergencyContact = mapDtoToEntity(emergencyContactDto);
        EmergencyContact emergencyContactDB = emergencyContactRepository.save(emergencyContact);

        return mapEntityToDto(emergencyContactDB);
    }

    @Override
    public EmergencyContactDto update(EmergencyContactDto emergencyContactDto) {
        return emergencyContactRepository.findById(UUID.fromString(emergencyContactDto.getId()))
            .map(emergencyContact -> updateEntityWithDataFromDto(emergencyContact, emergencyContactDto))
            .map(emergencyContactRepository::save)
            .map(this::mapEntityToDto)
            .orElseGet(() -> save(emergencyContactDto));
    }

    @Override
    public void deleteById(String id) {
        emergencyContactRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public Optional<EmergencyContactDto> findByUUID(String id) {
        return emergencyContactRepository.findById(UUID.fromString(id))
            .map(this::mapEntityToDto);
    }

    @Override
    public List<EmergencyContactDto> findAll() {
        return emergencyContactRepository.findAll()
            .stream()
            .map(this::mapEntityToDto)
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

    private EmergencyContactDto mapEntityToDto(EmergencyContact emergencyContact) {
        return EmergencyContactDto.builder()
            .id(String.valueOf(emergencyContact.getId()))
            .name(emergencyContact.getName())
            .surname(emergencyContact.getSurname())
            .address(emergencyContact.getAddress())
            .email(emergencyContact.getEmail())
            .telephoneNumber(emergencyContact.getTelephoneNumber())
            .build();
    }

    private EmergencyContact updateEntityWithDataFromDto(EmergencyContact emergencyContact, EmergencyContactDto emergencyContactDto) {
        emergencyContact.setAddress(emergencyContactDto.getAddress());
        emergencyContact.setEmail(emergencyContactDto.getEmail());
        emergencyContact.setSurname(emergencyContactDto.getSurname());
        emergencyContact.setName(emergencyContactDto.getName());
        emergencyContact.setName(emergencyContactDto.getName());
        emergencyContact.setTelephoneNumber(emergencyContactDto.getTelephoneNumber());

        return emergencyContact;
    }
}
