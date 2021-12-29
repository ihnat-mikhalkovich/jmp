package com.epam.learn.nosql.taskmanager.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class DocumentIdSerializer extends StdSerializer<String> {

    public DocumentIdSerializer() {
        super(String.class);
    }

    protected DocumentIdSerializer(Class<String> vc) {
        super(vc);
    }

    protected DocumentIdSerializer(JavaType valueType) {
        super(valueType);
    }

    protected DocumentIdSerializer(StdSerializer src) {
        super(src);
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value != null) {
            gen.writeStartObject();
            gen.writeStringField("$oid", value);
            gen.writeEndObject();
        }
    }
}
