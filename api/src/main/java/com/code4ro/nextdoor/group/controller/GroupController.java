package com.code4ro.nextdoor.group.controller;

import com.code4ro.nextdoor.group.dto.GroupCreateDto;
import com.code4ro.nextdoor.group.dto.GroupDto;
import com.code4ro.nextdoor.group.dto.GroupUpdateDto;
import com.code4ro.nextdoor.group.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Api("Group Controller")
@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(final GroupService groupService) {
        this.groupService = groupService;
    }

    @ApiOperation("Create group")
    @PostMapping
    public ResponseEntity<GroupDto> create(@RequestBody final GroupCreateDto createDto) {
        final GroupDto groupDto = groupService.create(createDto);
        return ResponseEntity.ok(groupDto);
    }

    @ApiOperation("Update group")
    @PutMapping("/{id}")
    public ResponseEntity<GroupDto> update(@PathVariable("id") final UUID id,
                                           @RequestBody final GroupUpdateDto updateDto) {
        final GroupDto groupDto = groupService.update(id, updateDto);
        return ResponseEntity.ok(groupDto);
    }

    @ApiOperation("Get group")
    @GetMapping("/{id}")
    public ResponseEntity<GroupDto> get(@PathVariable("id") final UUID id) {
        final GroupDto groupDto = groupService.get(id);
        return ResponseEntity.ok(groupDto);
    }
}
