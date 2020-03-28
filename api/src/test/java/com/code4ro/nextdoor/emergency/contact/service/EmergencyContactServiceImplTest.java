package com.code4ro.nextdoor.emergency.contact.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.code4ro.nextdoor.emergency.contact.dto.EmergencyContactDto;
import com.code4ro.nextdoor.emergency.contact.entity.EmergencyContact;
import com.code4ro.nextdoor.emergency.contact.mapper.EmergencyContactMapper;
import com.code4ro.nextdoor.emergency.contact.repository.EmergencyContactRepository;
import com.code4ro.nextdoor.emergency.contact.service.impl.EmergencyContactServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmergencyContactServiceImplTest {

    private static final String ID = "14022837-be98-4457-af25-075d7a136a33";
    private static final UUID UID = UUID.fromString(ID);

    @InjectMocks
    private EmergencyContactServiceImpl underTest;

    @Mock
    private EmergencyContactRepository emergencyContactRepository;

    @Mock
    private EmergencyContactMapper emergencyContactMapper;

    private static EmergencyContact emergencyContact =
        EmergencyContact.builder()
        .address("address")
        .email("email@something.com")
        .name("name")
        .surname("surname")
        .telephoneNumber("07xx43x56x")
        .build();

    @Test
    public void testFindAll() {
        when(emergencyContactRepository.findAll()).
            thenReturn(Arrays.asList(emergencyContact));

        List<EmergencyContactDto> result = underTest.findAll();

        assertEquals(1, result.size());
        assertEquals(emergencyContact.getAddress(), result.get(0).getAddress());
        assertEquals(emergencyContact.getEmail(), result.get(0).getEmail());
        assertEquals(emergencyContact.getName(), result.get(0).getName());
        assertEquals(emergencyContact.getSurname(), result.get(0).getSurname());
        assertEquals(emergencyContact.getTelephoneNumber(), result.get(0).getTelephoneNumber());
    }

    @Test
    public void testFindById() {
        when(emergencyContactRepository.findById(UID)).
            thenReturn(Optional.of(getEmergencyContactWithId(emergencyContact)));

        EmergencyContactDto result = underTest.findByUUID(ID);

        assertEquals(emergencyContact.getAddress(), result.getAddress());
        assertEquals(emergencyContact.getEmail(), result.getEmail());
        assertEquals(emergencyContact.getName(), result.getName());
        assertEquals(emergencyContact.getSurname(), result.getSurname());
        assertEquals(emergencyContact.getTelephoneNumber(), result.getTelephoneNumber());
    }

    @Test
    public void testFindByNonExistingId() {
        when(emergencyContactRepository.findById(UID)).
            thenReturn(Optional.empty());

        EmergencyContactDto result = underTest.findByUUID(ID);

        assertNull(result);
    }

    @Test
    public void testDeleteById() {
        when(emergencyContactRepository.findById(UID))
            .thenReturn(Optional.of(getEmergencyContactWithId(emergencyContact)));
        doNothing().when(emergencyContactRepository).deleteById(UID);

        underTest.deleteById(ID);

        verify(emergencyContactRepository, times(1)).deleteById(UID);
    }

    private EmergencyContact getEmergencyContactWithId(EmergencyContact emergencyContact) {
        emergencyContact.setId(UID);

        return emergencyContact;
    }
}
