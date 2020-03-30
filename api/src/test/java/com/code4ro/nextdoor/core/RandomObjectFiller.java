package com.code4ro.nextdoor.core;

import com.code4ro.nextdoor.core.entity.BaseEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class RandomObjectFiller {
    public static <T> T createAndFill(Class<T> clazz) {
        try {
            final T instance = clazz.getDeclaredConstructor().newInstance();
            for (final Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = getRandomValueForField(field);
                field.set(instance, value);
            }
            return instance;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T createAndFillWithBaseEntity(Class<? extends BaseEntity> clazz) {
        final T instance = (T) createAndFill(clazz);
        ((BaseEntity) instance).setId(UUID.randomUUID());
        return instance;
    }

    private static Object getRandomValueForField(Field field) {
        final Class<?> type = field.getType();

        if (type.isEnum()) {
            Object[] enumValues = type.getEnumConstants();
            return enumValues[RandomUtils.nextInt(0, enumValues.length)];
        } else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return RandomUtils.nextInt();
        } else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
            return RandomUtils.nextLong();
        } else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
            return RandomUtils.nextDouble();
        } else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
            return RandomUtils.nextFloat();
        } else if (type.equals(UUID.class)) {
            return UUID.randomUUID();
        } else if (type.equals(BigInteger.class)) {
            return BigInteger.valueOf(RandomUtils.nextInt());
        } else if (type.equals(String.class)) {
            return getFieldWithContraint(field);
        } else if (type.equals(Boolean.class)) {
            return false;
        } else if (type.equals(Date.class)) {
            int randomYear = RandomUtils.nextInt(1990, 2020);
            int randomDay = RandomUtils.nextInt(1, 366);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, randomYear);
            calendar.set(Calendar.DAY_OF_YEAR, randomDay);
            return calendar.getTime();
        }
        return createAndFill(type);
    }

    private static String getFieldWithContraint(final Field field) {
        final Email email = field.getAnnotation(Email.class);
        if (email != null) {
            return RandomStringUtils.randomAlphabetic(10) + "@email.com";
        }
        final Size size = field.getAnnotation(Size.class);
        if (size != null) {
            return RandomStringUtils.randomAlphabetic(size.min(), size.max());
        }
        return RandomStringUtils.randomAlphabetic(10);
    }
}
