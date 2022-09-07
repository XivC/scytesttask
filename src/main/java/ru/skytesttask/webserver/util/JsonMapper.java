package ru.skytesttask.webserver.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonMapper<T> {

    private final Class<T> objectClass;
    private final ObjectMapper mapper;

    public JsonMapper(Class<T> objectClass) {
        this.objectClass = objectClass;
        this.mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.findAndRegisterModules();
    }

    public T getObject(String json) throws IOException {
        return mapper.readValue(json, objectClass);
    }

    public String getJson(T object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}
