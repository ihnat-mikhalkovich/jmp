package com.epam.learn.restapi.util.mapper;

import com.epam.learn.restapi.dto.Event;
import com.epam.learn.restapi.dto.EventDto;
import org.mapstruct.Mapper;

@Mapper
public interface EventMapper {
    Event toModel(EventDto eventDto);
    EventDto toDto(Event event);
}
