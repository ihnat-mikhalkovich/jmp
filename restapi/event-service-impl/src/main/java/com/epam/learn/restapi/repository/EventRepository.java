package com.epam.learn.restapi.repository;

import com.epam.learn.restapi.dto.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {
    List<Event> findAllByTitleContainsIgnoreCase(String title);
}
