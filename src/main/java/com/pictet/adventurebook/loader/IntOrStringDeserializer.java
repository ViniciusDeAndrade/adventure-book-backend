package com.pictet.adventurebook.loader;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class IntOrStringDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        switch (p.getCurrentToken()) {
            case VALUE_STRING:
                return Integer.parseInt(p.getText().trim());
            case VALUE_NUMBER_INT:
                return p.getIntValue();
            default:
                return (Integer) ctxt.handleUnexpectedToken(Integer.class, p);
        }
    }
}
