package com.code4ro.nextdoor.group;

import com.code4ro.nextdoor.core.service.MapperService;
import com.code4ro.nextdoor.group.dto.GroupCreateDto;
import com.code4ro.nextdoor.group.dto.GroupUpdateDto;
import com.code4ro.nextdoor.group.entity.Group;
import com.code4ro.nextdoor.group.repository.GroupRepository;
import com.code4ro.nextdoor.group.service.impl.GroupServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GroupServiceTest {
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private MapperService mapperService;

    @InjectMocks
    private GroupServiceImpl groupService;
    @Captor
    private ArgumentCaptor<Group> groupArgumentCaptor;

    @Test
    public void createOpenGroup() {
        final GroupCreateDto createDto = GroupFactory.createDto();
        final Group group = GroupFactory.createOpenGroup();

        when(mapperService.map(createDto, Group.class)).thenReturn(group);

        groupService.create(createDto);

        verify(groupRepository).save(groupArgumentCaptor.capture());
        assertThat(groupArgumentCaptor.getValue().getSecurityPolicy()).isNull();
    }

    @Test
    public void createClosedGroup() {
        final GroupCreateDto createDto = GroupFactory.createDto();
        final Group group = GroupFactory.createClosedGroup();

        when(mapperService.map(createDto, Group.class)).thenReturn(group);

        groupService.create(createDto);

        verify(groupRepository).save(groupArgumentCaptor.capture());
        assertThat(groupArgumentCaptor.getValue().getSecurityPolicy()).isNotNull();
    }

    @Test(expected = EntityNotFoundException.class)
    public void updateNonExistentGroup() {
        final GroupUpdateDto updateDto = GroupFactory.updateDto();

        when(groupRepository.findById(any())).thenReturn(Optional.empty());

        groupService.update(UUID.randomUUID(), updateDto);
    }

    @Test
    public void updateExistingGroup() {
        final GroupUpdateDto updateDto = GroupFactory.updateDto();

        when(groupRepository.findById(any())).thenReturn(Optional.of(GroupFactory.entity()));

        groupService.update(UUID.randomUUID(), updateDto);
        verify(groupRepository).save(groupArgumentCaptor.capture());
        final Group updatedGroup = groupArgumentCaptor.getValue();

        assertThat(updatedGroup.getName()).isEqualTo(updateDto.getName());
        assertThat(updatedGroup.getDescription()).isEqualTo(updateDto.getDescription());
        assertThat(updatedGroup.getOpen()).isEqualTo(updateDto.getOpen());
    }

    @Test(expected = EntityNotFoundException.class)
    public void getNonExistingGroup() {
        when(groupRepository.findById(any())).thenReturn(Optional.empty());

        groupService.get(UUID.randomUUID());
    }

    @Test
    public void getExistingGroup() {
        when(groupRepository.findById(any())).thenReturn(Optional.of(GroupFactory.entity()));

        final UUID requestedId = UUID.randomUUID();
        groupService.get(requestedId);

        verify(groupRepository).findById(requestedId);
    }
}
