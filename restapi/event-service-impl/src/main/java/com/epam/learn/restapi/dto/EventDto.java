package com.epam.learn.restapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.*;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class EventDto extends RepresentationModel<EventDto> {

    @Schema(example = "1", description = "Unique event identifier")
    @PositiveOrZero(message = "'id' should be positive (from 1 to max long value) or by default '0'.")
    private long id;

    @Schema(example = "Java 17 is closer than you think", description = "Subject of event. Title of event.")
    @NotBlank(message = "'title' should not be blank.")
    @Length(max = 256, message = "'title' should not be longer then 256.")
    private String title;

    @Schema(example = "Staples center", description = "Event venue")
    @NotBlank(message = "'place' should not be blank.")
    @Length(max = 256, message = "'place' should not be longer then 256.")
    private String place;

    @Schema(example = "Dwayne Rock Johnson", description = "The person how will do knowledge transfer")
    @NotBlank(message = "'speaker' should not be blank.")
    @Length(max = 256, message = "'speaker' should not be longer then 256.")
    private String speaker;

    @Schema(example = "offline", description = "The type of event. [offline, online, on-fresh-air]")
    @NotBlank(message = "'eventType' should not be blank.")
    @Length(max = 50, message = "'eventType' should not be longer then 50.")
    private String eventType;

    @Schema(example = "2021-05-20T15:00:00.000+03:00", description = "The date of event")
    @NotNull(message = "'dateTime' should not be null.")
    @Future(message = "'`dateTime' should be in future.")
    private ZonedDateTime dateTime;
}
