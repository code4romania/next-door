package com.code4ro.nextdoor.emergency.contact.service;

import com.code4ro.nextdoor.emergency.contact.dto.EmergencyContactDto;
import java.util.List;

public interface EmergencyContactService {

    EmergencyContactDto save(EmergencyContactDto emergencyContactDto);

    EmergencyContactDto update(EmergencyContactDto emergencyContactDto);

    void deleteById(String id);

    EmergencyContactDto findByUUID(String id);

    List<EmergencyContactDto> findAll();
}
