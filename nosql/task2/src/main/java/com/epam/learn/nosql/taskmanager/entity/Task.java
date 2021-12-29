package com.epam.learn.nosql.taskmanager.entity;

import com.epam.learn.nosql.taskmanager.util.json.DocumentIdDeserializer;
import com.epam.learn.nosql.taskmanager.util.json.DocumentIdSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Task {
    @JsonProperty("_id")
    @JsonSerialize(using = DocumentIdSerializer.class)
    @JsonDeserialize(using = DocumentIdDeserializer.class)
    private String id;

    private ZonedDateTime dateOfCreation;
    private ZonedDateTime deadline;
    private String name;
    private String description;
    private List<Subtask> subtasks;
    private String category;
}
