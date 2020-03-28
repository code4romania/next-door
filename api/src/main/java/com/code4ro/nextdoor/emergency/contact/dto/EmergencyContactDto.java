package com.code4ro.nextdoor.emergency.contact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContactDto {
    private String id;

    private String name;
    private String surname;
    private String email;
    private String address;
    private String telephoneNumber;
}
