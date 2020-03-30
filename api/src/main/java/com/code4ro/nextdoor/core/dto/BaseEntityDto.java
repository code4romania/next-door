package com.code4ro.nextdoor.core.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class BaseEntityDto {
    private UUID id;
    private LocalDateTime creationDate;
}
