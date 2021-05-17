package com.epam.learn.restapi.service;

import com.epam.learn.restapi.dto.Event;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface EventService {

    @NonNull
    Event createEvent(@NonNull Event event);

    @NonNull
    Optional<Event> updateEvent(@NonNull Event event);

    @NonNull
    Optional<Event> getEvent(@NonNull Event event);

    void deleteEvent(@NonNull Event event);

    @NonNull
    List<Event> getAllEvents();

    @NonNull
    List<Event> getAllEventsByTitle(@NonNull String title);
}
