package com.code4ro.nextdoor.core.service.impl;

import com.code4ro.nextdoor.core.dto.BaseEntityDto;
import com.code4ro.nextdoor.core.entity.BaseEntity;
import com.code4ro.nextdoor.core.service.MapperService;
import com.code4ro.nextdoor.emergency.contact.dto.EmergencyContactDto;
import com.code4ro.nextdoor.emergency.contact.entity.EmergencyContact;
import com.code4ro.nextdoor.group.dto.GroupDto;
import com.code4ro.nextdoor.group.dto.GroupSecurityPolicyDto;
import com.code4ro.nextdoor.group.entity.Group;
import com.code4ro.nextdoor.group.entity.GroupSecurityPolicy;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapperServiceImpl implements MapperService, ApplicationListener<ContextRefreshedEvent> {
    private final ModelMapper modelMapper;

    public MapperServiceImpl() {
        this.modelMapper = new ModelMapper();

        addCustomMappings();
        addCustomTypeMaps();
    }

    private void addCustomMappings() {
        modelMapper.createTypeMap(BaseEntity.class, BaseEntityDto.class)
                .addMapping(BaseEntity::getId, BaseEntityDto::setId);
        modelMapper.createTypeMap(BaseEntityDto.class, BaseEntity.class)
                .addMapping(BaseEntityDto::getId, BaseEntity::setId);
    }

    private void addCustomTypeMaps() {
        modelMapper.createTypeMap(Group.class, GroupDto.class)
                .includeBase(BaseEntity.class, BaseEntityDto.class);

        modelMapper.createTypeMap(GroupSecurityPolicy.class, GroupSecurityPolicyDto.class);

        modelMapper.createTypeMap(EmergencyContact.class, EmergencyContactDto.class);
        modelMapper.createTypeMap(EmergencyContactDto.class, EmergencyContact.class);
    }

    @Override
    public <T> T map(final Object source, final Class<T> targetType) {
        return source != null ? modelMapper.map(source, targetType) : null;
    }

    @Override
    public <T> List<T> mapList(final List<?> sourceList, final Class<? extends T> targetClass) {
        if (sourceList == null) {
            return Collections.emptyList();
        }

        return sourceList.stream()
                .map(listElement -> modelMapper.map(listElement, targetClass))
                .collect(Collectors.toList());
    }

    @Override
    public <T> Page<T> mapPage(final Page<?> sourcePage, final Class<? extends T> targetClass) {
        if (sourcePage == null) {
            return Page.empty();
        }

        return sourcePage.map(pageElement -> modelMapper.map(pageElement, targetClass));
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        final Collection<AbstractConverter> converters =
                event.getApplicationContext().getBeansOfType(AbstractConverter.class).values();
        converters.forEach(modelMapper::addConverter);
    }
}
