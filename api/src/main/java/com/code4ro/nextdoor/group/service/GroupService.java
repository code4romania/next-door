package com.code4ro.nextdoor.group.service;

import com.code4ro.nextdoor.group.dto.GroupCreateDto;
import com.code4ro.nextdoor.group.dto.GroupDto;
import com.code4ro.nextdoor.group.dto.GroupUpdateDto;

import java.util.UUID;

public interface GroupService {
    GroupDto create(GroupCreateDto createDto);

    GroupDto update(UUID id, GroupUpdateDto updateDto);

    GroupDto get(UUID id);
}
