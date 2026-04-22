package io.github.lukete.serviceflow.support;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonSupport extends IntegrationTestBase {
    @Autowired
    protected ObjectMapper objectMapper;

    protected String toJson(Object value) throws Exception {
        return objectMapper.writeValueAsString(value);
    }
}
