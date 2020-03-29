package com.code4ro.nextdoor.group.repository;

import com.code4ro.nextdoor.group.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {
}
