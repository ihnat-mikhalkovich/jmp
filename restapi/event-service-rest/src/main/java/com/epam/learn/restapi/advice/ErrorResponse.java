package com.epam.learn.restapi.advice;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    @Schema(example = "2021-05-17T03:23:31.106+03:00", description = "Time when the error registered")
    private ZonedDateTime timestamp;
    @Schema(example = "Bad Request", description = "Is a brief description of error")
    private String error;
    @Schema(example = "400", description = "Error status code.",
            externalDocs = @ExternalDocumentation(description = "List of HTTP status codes", url = "https://en.wikipedia.org/wiki/List_of_HTTP_status_codes"))
    private int status;
    @Schema(example = "Bad Request. Failed on validation", description = "Full description of error")
    private Object message;
    @Schema(example = "uri=/api/v1/events/-1", description = "Url, where the user got the error")
    private String path;
}
