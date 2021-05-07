package com.tp.matlab.kernel.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by tangpeng on 2021-01-01
 */
@Slf4j
@UtilityClass
public class ObjectMapperUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static ObjectMapper getMapper() {
        return MAPPER;
    }


    public static String asStringSerenely(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("[ObjectMapper] failed to convert object to JSON string.", e);
            return "";
        }
    }

    public static <T> T toValue(
            Object fromValue, Class<T> toValueType
    ) throws JsonProcessingException {
        try {
            return MAPPER.convertValue(fromValue, toValueType);
        } catch (IllegalArgumentException e) {
            throw new JsonGenericException(e);
        }
    }

    public static <T> T asValue(
            String value, Class<T> valueType
    ) throws JsonProcessingException {
        try {
            return MAPPER.readValue(value, valueType);
        } catch (IOException e) {
            throw new JsonGenericException(e);
        }
    }

    public static <T> T asValue(
            String value, TypeReference<T> valueTypeRef
    ) throws JsonProcessingException {
        try {
            return MAPPER.readValue(value, valueTypeRef);
        } catch (IOException e) {
            throw new JsonGenericException(e);
        }
    }

    public static <T> T toValue(
            Object fromValue, TypeReference<T> toValueTypeRef
    ) throws JsonProcessingException {
        try {
            return MAPPER.convertValue(fromValue, toValueTypeRef);
        } catch (IllegalArgumentException e) {
            throw new JsonGenericException(e);
        }
    }


    /**
     * Created by tangpeng on 2021-01-01
     */
    class JsonGenericException extends JsonProcessingException {

        protected JsonGenericException(String message) {
            super(message);
        }

        protected JsonGenericException(final String message, final Throwable cause) {
            super(message, cause);
        }

        protected JsonGenericException(Throwable cause) {
            super(cause);
        }

    }
}
