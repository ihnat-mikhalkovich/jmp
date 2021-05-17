package com.epam.learn.restapi.controller;

import com.epam.learn.restapi.advice.ErrorResponse;
import com.epam.learn.restapi.dto.Event;
import com.epam.learn.restapi.dto.EventDto;
import com.epam.learn.restapi.service.EventService;
import com.epam.learn.restapi.util.mapper.EventMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(EventServiceController.API_V_1_EVENT)
@RequiredArgsConstructor
@Validated
@Tag(name = "Event API", description = "Operations that related with events")
public class EventServiceController {
    public static final String API_V_1_EVENT = "/api/v1/events/";

    private final EventService eventService;

    private final EventMapper eventMapper;

    @Operation(summary = "Get all events when 'title' is absent. Get events by title or by short piece of title when 'title' is present")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EventDto.class)))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema()))})
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<EventDto>> findAllEvents(@Parameter(in = ParameterIn.QUERY, description = "Title of event, or part of event title. Case Insensitive.", required = false, example = "java") @RequestParam(required = false) @Length(max = 256) String title) {
        final List<Event> events;
        if (title == null) {
            events = eventService.getAllEvents();
        } else {
            events = eventService.getAllEventsByTitle(title);
        }

        final List<EventDto> eventDtoList = events.stream()
                .map(eventMapper::toDto)
                .map(eventDto -> eventDto.add(
                        linkTo(methodOn(EventServiceController.class)
                                .getEventById(eventDto.getId()))
                                .withSelfRel()
                ))
                .collect(Collectors.toList());

        final Link selfLink = linkTo(methodOn(EventServiceController.class)
                .findAllEvents(title))
                .withSelfRel();
        final CollectionModel<EventDto> collectionModel = CollectionModel.of(eventDtoList, selfLink);
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get event by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response", content = @Content(schema = @Schema(implementation = EventDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid provided Id", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Event not found by id", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema()))})
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDto> getEventById(@Parameter(in = ParameterIn.PATH, description = "Id of event", required = true, example = "1") @PathVariable @Positive long id) {
        final Optional<EventDto> eventDto = eventService.getEvent(Event.builder().id(id).build())
                .map(eventMapper::toDto)
                .map(it -> it.add(
                        linkTo(methodOn(EventServiceController.class)
                                .getEventById(id))
                                .withSelfRel()
                ));
        return ResponseEntity.of(eventDto);
    }

    @Operation(summary = "Update an event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response. Return updated event.", content = @Content(schema = @Schema(implementation = EventDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, data has failed validation", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Event not found by id", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema()))})
    @PutMapping(path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDto> updateEvent(@Parameter(in = ParameterIn.DEFAULT, description = "New event that will replace old event", required = true) @RequestBody @Valid EventDto eventDto) {
        return eventService.updateEvent(eventMapper.toModel(eventDto))
                .map(eventMapper::toDto)
                .map(it -> it.add(
                        linkTo(methodOn(EventServiceController.class)
                                .getEventById(it.getId()))
                                .withSelfRel()
                ))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Create an event")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful response. Return new event.", content = @Content(schema = @Schema(implementation = EventDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, data has failed validation", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema()))})
    @PostMapping(path = "",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EventDto> createEvent(@Parameter(in = ParameterIn.DEFAULT, description = "Data about event", required = true) @RequestBody @Valid EventDto eventDto) {
        final Event event = eventService.createEvent(eventMapper.toModel(eventDto));
        final EventDto newEventDto = eventMapper.toDto(event);
        newEventDto.add(
                linkTo(methodOn(EventServiceController.class).getEventById(newEventDto.getId())).withSelfRel()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(newEventDto);
    }

    @Operation(summary = "Delete event by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content, event have deleted"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid provided Id", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema()))})
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> deleteEvent(@Parameter(in = ParameterIn.PATH, description = "Id of event", required = true, example = "1") @PathVariable @Positive long id) {
        final Event event = new Event();
        event.setId(id);
        eventService.deleteEvent(event);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
