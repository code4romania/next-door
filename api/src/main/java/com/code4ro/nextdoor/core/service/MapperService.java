package com.code4ro.nextdoor.core.service;

import org.springframework.data.domain.Page;

import java.util.List;

public interface MapperService {
    <T> T map(Object source, Class<T> targetType);

    <T> List<T> mapList(List<?> sourceList, Class<? extends T> targetClass);

    <T> Page<T> mapPage(Page<?> sourcePage, Class<? extends T> targetClass);
}
