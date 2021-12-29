package com.epam.learn.nosql.taskmanager.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.bson.types.ObjectId;

import java.io.IOException;

public class DocumentIdDeserializer extends StdDeserializer<String> {

    public DocumentIdDeserializer() {
        super(String.class);
    }

    protected DocumentIdDeserializer(Class<String> vc) {
        super(vc);
    }

    protected DocumentIdDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected DocumentIdDeserializer(StdDeserializer src) {
        super(src);
    }

    @Override
    public String deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonNode node = jp.getCodec().readTree(jp);
        final JsonNode oid = node.get("$oid");
        if (oid != null) {
            return oid.asText();
        } else if (node.has("timestamp")) {
            final int timestamp = node.get("timestamp").intValue();
            final int counter = node.get("counter").intValue();
            return new ObjectId(timestamp, counter).toString();
        } else {
            return null;
        }
    }
}
