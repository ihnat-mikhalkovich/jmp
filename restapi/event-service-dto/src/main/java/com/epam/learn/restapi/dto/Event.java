package com.epam.learn.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    private String title;

    private String place;

    private String speaker;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "date_time")
    private ZonedDateTime dateTime;
}
