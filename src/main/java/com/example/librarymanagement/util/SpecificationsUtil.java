package com.example.librarymanagement.util;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.exception.BadRequestException;

import java.util.ArrayList;
import java.util.List;

public class SpecificationsUtil {
    public static Object castToRequiredType(Class<?> fieldType, String value) {
        try {
            if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                return Double.parseDouble(value);
            } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                return Float.parseFloat(value);
            } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                return Long.parseLong(value);
            } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                return Integer.parseInt(value);
            } else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
                return Byte.parseByte(value);
            } else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                return Short.parseShort(value);
            } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                return Boolean.parseBoolean(value);
            } else {
                return value;
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException(ErrorMessage.INVALID_NUMBER_FORMAT);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> castListToRequiredType2(Class<T> fieldType, List<String> values) {
        List<T> result = new ArrayList<>();
        for (String value : values) {
            result.add((T) castToRequiredType(fieldType, value));
        }
        return result;
    }

}
