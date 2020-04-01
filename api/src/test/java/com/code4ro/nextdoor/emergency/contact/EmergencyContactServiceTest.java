package com.code4ro.nextdoor.emergency.contact;

import static com.code4ro.nextdoor.emergency.contact.EmergencyContactFactory.createEntityList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.code4ro.nextdoor.core.service.MapperService;
import com.code4ro.nextdoor.emergency.contact.dto.EmergencyContactDto;
import com.code4ro.nextdoor.emergency.contact.entity.EmergencyContact;
import com.code4ro.nextdoor.emergency.contact.repository.EmergencyContactRepository;
import com.code4ro.nextdoor.emergency.contact.service.impl.EmergencyContactServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmergencyContactServiceTest {

    private static final String ID = "14022837-be98-4457-af25-075d7a136a33";

    @InjectMocks
    private EmergencyContactServiceImpl underTest;

    @Mock
    private EmergencyContactRepository emergencyContactRepository;

    @Mock
    private MapperService mapperService;

    @Test
    @DisplayName("Save emergency contact")
    public void testSave() {
        final EmergencyContactDto createDto = EmergencyContactFactory.createDto();
        final EmergencyContact entity = EmergencyContactFactory.createEntity();

        when(mapperService.map(createDto, EmergencyContact.class)).thenReturn(entity);
        when(emergencyContactRepository.save(entity)).thenReturn(entity);
        when(mapperService.map(entity, EmergencyContactDto.class)).thenReturn(createDto);

        EmergencyContactDto result = underTest.save(createDto);

        verify(emergencyContactRepository).save(entity);
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(createDto);
    }

    @Test
    @DisplayName("Search and find an emergency contact by UUID")
    public void testFindByUUID() {
        final EmergencyContactDto dto = EmergencyContactFactory.createDto();
        final EmergencyContact entity = EmergencyContactFactory.createEntity();

        when(emergencyContactRepository.findById(UUID.fromString(ID))).thenReturn(Optional.of(entity));
        when(mapperService.map(entity, EmergencyContactDto.class)).thenReturn(dto);

        Optional<EmergencyContactDto> result = underTest.findByUUID(ID);

        verify(emergencyContactRepository).findById(UUID.fromString(ID));
        assertThat(result).isNotNull();
        assertThat(result.get()).isEqualTo(dto);
    }

    @Test
    @DisplayName("Search an emergency contact by a non existing UUID in DB")
    public void testFindByNonExistingUUID() {
        when(emergencyContactRepository.findById(UUID.fromString(ID))).thenReturn(Optional.empty());

        Optional<EmergencyContactDto> result = underTest.findByUUID(ID);

        verify(emergencyContactRepository).findById(UUID.fromString(ID));
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Delete emergency contact by id")
    public void testDeleteById() {
        doNothing().when(emergencyContactRepository).deleteById(UUID.fromString(ID));

        underTest.deleteById(ID);

        verify(emergencyContactRepository).deleteById(UUID.fromString(ID));
    }

    @Test
    @DisplayName("Find all emergency contacts in database")
    public void testFindAll() {
        List<EmergencyContact> entityList = createEntityList();

        when(emergencyContactRepository.findAll()).thenReturn(entityList);

        List<EmergencyContactDto> result = underTest.findAll();

        verify(emergencyContactRepository).findAll();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(entityList.size());
    }
}
