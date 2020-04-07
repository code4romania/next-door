package com.code4ro.nextdoor.group.entity;


import com.code4ro.nextdoor.core.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "nd_groups")
@Entity
@Getter
@Setter
public class Group extends BaseEntity {
    private String name;
    private String description;
    private Boolean open;
    @Cascade(CascadeType.ALL)
    @OneToOne(fetch = FetchType.LAZY)
    private GroupSecurityPolicy securityPolicy;
}
