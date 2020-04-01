package com.code4ro.nextdoor.emergency.contact;

import com.code4ro.nextdoor.core.RandomObjectFiller;
import com.code4ro.nextdoor.emergency.contact.dto.EmergencyContactDto;
import com.code4ro.nextdoor.emergency.contact.entity.EmergencyContact;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EmergencyContactFactory {

    public static EmergencyContact createEntity() {
        return RandomObjectFiller.createAndFill(EmergencyContact.class);
    }

    public static EmergencyContactDto createDto() {
        return RandomObjectFiller.createAndFill(EmergencyContactDto.class);
    }

    public static List<EmergencyContact> createEntityList() {
        return IntStream.range(1, 10)
            .boxed()
            .map(element ->  RandomObjectFiller.createAndFill(EmergencyContact.class))
            .collect(Collectors.toList());
    }

    public static List<EmergencyContactDto> createDtoList() {
        return IntStream.range(1, 10)
            .boxed()
            .map(element ->  RandomObjectFiller.createAndFill(EmergencyContactDto.class))
            .collect(Collectors.toList());
    }
}
