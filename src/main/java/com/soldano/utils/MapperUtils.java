package com.soldano.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soldano.exception.MappingException;

public final class MapperUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MapperUtils() {
    }

    public static <T> T mapFromJsonString(String from, Class<T> type) {
        try {
            return objectMapper.readValue(from, type);
        } catch (JsonProcessingException e) {
            throw new MappingException("Error while mapping object");
        }
    }

    public static String mapToJsonString(Object from) {
        try {
            return objectMapper.writeValueAsString(from);
        } catch (JsonProcessingException e) {
            throw new MappingException("Error while mapping object");
        }
    }
}
