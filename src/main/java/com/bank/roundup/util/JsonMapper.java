package com.bank.roundup.util;

import com.fasterxml.jackson.databind.ObjectMapper;

// Singleton so only one object mapper needs to be created for the whole system
public class JsonMapper {

    private static final ObjectMapper objectMapperInstance = constructObjectMapper();

    private static ObjectMapper constructObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        return objectMapper;
    }

    private JsonMapper() {

    }

    public static ObjectMapper getObjectMapperInstance() {
        return objectMapperInstance;
    }
}