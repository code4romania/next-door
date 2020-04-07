package com.code4ro.nextdoor.group.service.impl;

import com.code4ro.nextdoor.core.service.MapperService;
import com.code4ro.nextdoor.group.dto.GroupCreateDto;
import com.code4ro.nextdoor.group.dto.GroupDto;
import com.code4ro.nextdoor.group.dto.GroupUpdateDto;
import com.code4ro.nextdoor.group.entity.Group;
import com.code4ro.nextdoor.group.entity.GroupSecurityPolicy;
import com.code4ro.nextdoor.group.repository.GroupRepository;
import com.code4ro.nextdoor.group.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final MapperService mapperService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public GroupServiceImpl(final GroupRepository groupRepository,
                            final MapperService mapperService,
                            final PasswordEncoder passwordEncoder) {
        this.groupRepository = groupRepository;
        this.mapperService = mapperService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public GroupDto create(final GroupCreateDto createDto) {
        final Group group = mapperService.map(createDto, Group.class);
        if (group.getOpen()) {
            group.setSecurityPolicy(null);
        } else {
            final GroupSecurityPolicy securityPolicy = group.getSecurityPolicy();
            final String rawAnswer = securityPolicy.getAnswer();
            securityPolicy.setAnswer(passwordEncoder.encode(rawAnswer));
        }

        final Group savedGroup = groupRepository.save(group);
        return mapperService.map(savedGroup, GroupDto.class);
    }

    @Override
    public GroupDto update(final UUID id, final GroupUpdateDto updateDto) {
        final Group group = groupRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        group.setName(updateDto.getName());
        group.setDescription(updateDto.getDescription());
        group.setOpen(updateDto.getOpen());

        final Group savedGroup = groupRepository.save(group);
        return mapperService.map(savedGroup, GroupDto.class);
    }

    @Override
    public GroupDto get(final UUID id) {
        final Group group = groupRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return mapperService.map(group, GroupDto.class);
    }
}
