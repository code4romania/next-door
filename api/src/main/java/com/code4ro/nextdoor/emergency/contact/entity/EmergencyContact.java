package com.code4ro.nextdoor.emergency.contact.entity;

import com.code4ro.nextdoor.core.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContact extends BaseEntity {
    private String name;
    private String surname;
    @Column(unique = true)
    private String email;
    private String address;
    private String telephoneNumber;
}
