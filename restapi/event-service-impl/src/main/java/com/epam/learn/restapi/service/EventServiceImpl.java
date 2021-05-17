package com.epam.learn.restapi.service;

import com.epam.learn.restapi.repository.EventRepository;
import com.epam.learn.restapi.dto.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> updateEvent(Event event) {
        return Optional.of(eventRepository.save(event));
    }

    @Override
    public Optional<Event> getEvent(Event event) {
        return eventRepository.findById(event.getId());
    }

    @Override
    public void deleteEvent(Event event) {
        eventRepository.deleteById(event.getId());
    }

    @Override
    public List<Event> getAllEvents() {
        final Iterable<Event> events = eventRepository.findAll();
        return StreamSupport.stream(events.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Event> getAllEventsByTitle(String title) {
        return eventRepository.findAllByTitleContainsIgnoreCase(title);
    }
}
