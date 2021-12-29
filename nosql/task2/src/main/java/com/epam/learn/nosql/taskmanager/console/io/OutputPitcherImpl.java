package com.epam.learn.nosql.taskmanager.console.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutputPitcherImpl implements OutputPitcher {

    private final ObjectMapper objectMapper;

    @Override
    public void send(String message) {
        System.out.println(message);
    }

    @Override
    public void send(Object object) {
        String message;
        try {
            message = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            message = "The returned object can't be written as a json.";
        }
        System.out.println(message);
    }
}
