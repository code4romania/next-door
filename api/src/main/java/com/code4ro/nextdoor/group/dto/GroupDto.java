package com.code4ro.nextdoor.group.dto;

import com.code4ro.nextdoor.core.dto.BaseEntityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupDto extends BaseEntityDto {
    private String name;
    private String description;
    private Boolean open;
}
