package com.code4ro.nextdoor.group.entity;

import com.code4ro.nextdoor.core.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class GroupSecurityPolicy extends BaseEntity {
    private String question;
    private String answer;
}
