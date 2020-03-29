package com.code4ro.nextdoor.group;

import com.code4ro.nextdoor.core.RandomObjectFiller;
import com.code4ro.nextdoor.group.dto.GroupCreateDto;
import com.code4ro.nextdoor.group.dto.GroupUpdateDto;
import com.code4ro.nextdoor.group.entity.Group;

public class GroupFactory {

    public static Group createOpenGroup() {
        final Group group = RandomObjectFiller.createAndFill(Group.class);
        group.setOpen(true);
        return group;
    }

    public static Group createClosedGroup() {
        final Group group = RandomObjectFiller.createAndFill(Group.class);
        group.setOpen(false);
        return group;
    }

    public static Group entity() {
        return RandomObjectFiller.createAndFill(Group.class);
    }

    public static GroupCreateDto createDto() {
        return RandomObjectFiller.createAndFill(GroupCreateDto.class);
    }

    public static GroupUpdateDto updateDto() {
        return RandomObjectFiller.createAndFill(GroupUpdateDto.class);
    }
}
