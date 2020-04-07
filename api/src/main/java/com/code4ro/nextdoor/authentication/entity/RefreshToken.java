package com.code4ro.nextdoor.authentication.entity;

import com.code4ro.nextdoor.core.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RefreshToken extends BaseEntity {
    private String userId;
    private String token;
    private Date expiryDate;
}
