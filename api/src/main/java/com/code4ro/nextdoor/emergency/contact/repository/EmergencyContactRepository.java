package com.code4ro.nextdoor.emergency.contact.repository;

import com.code4ro.nextdoor.emergency.contact.entity.EmergencyContact;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, UUID> {
}
