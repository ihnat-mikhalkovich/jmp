package com.epam.learn.multithreading.pclineassembler.util;

import lombok.NonNull;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DetailsExtractor {
    public static Map<String, String> extractDetails(Map<String, String> details, @NonNull String... detailIds) {
        return details.entrySet().stream()
                .filter(entry ->
                        Stream.of(detailIds).anyMatch(detailId -> entry.getKey().contains(detailId)))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));
    }
}
