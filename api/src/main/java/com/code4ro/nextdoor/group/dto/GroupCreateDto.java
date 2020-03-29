package com.code4ro.nextdoor.group.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupCreateDto {
    private GroupSecurityPolicyDto securityPolicy;
    private String name;
    private String description;
    private Boolean open;
}
