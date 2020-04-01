package com.code4ro.nextdoor.emergency.contact.service.impl;

import com.code4ro.nextdoor.core.service.MapperService;
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
    private final MapperService mapperService;

    @Autowired
    public EmergencyContactServiceImpl(EmergencyContactRepository emergencyContactRepository, MapperService mapperService) {
        this.emergencyContactRepository = emergencyContactRepository;
        this.mapperService = mapperService;
    }

    @Override
    public EmergencyContactDto save(EmergencyContactDto emergencyContactDto) {
        final EmergencyContact emergencyContact = mapperService.map(emergencyContactDto, EmergencyContact.class);
        final EmergencyContact emergencyContactDB = emergencyContactRepository.save(emergencyContact);

        return mapperService.map(emergencyContactDB, EmergencyContactDto.class);
    }

    @Override
    public EmergencyContactDto update(EmergencyContactDto emergencyContactDto) {
        return emergencyContactRepository.findById(UUID.fromString(emergencyContactDto.getId()))
            .map(emergencyContact -> updateEntityWithDataFromDto(emergencyContact, emergencyContactDto))
            .map(emergencyContactRepository::save)
            .map(emergencyContact -> mapperService.map(emergencyContact, EmergencyContactDto.class))
            .orElseGet(() -> save(emergencyContactDto));
    }

    @Override
    public void deleteById(String id) {
        emergencyContactRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public Optional<EmergencyContactDto> findByUUID(String id) {
        return emergencyContactRepository.findById(UUID.fromString(id))
            .map(emergencyContact -> mapperService.map(emergencyContact, EmergencyContactDto.class));
    }

    @Override
    public List<EmergencyContactDto> findAll() {
        return emergencyContactRepository.findAll()
            .stream()
            .map(emergencyContact -> mapperService.map(emergencyContact, EmergencyContactDto.class))
            .collect(Collectors.toList());
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
