package ro.uaic.movie;

import java.sql.Date;

import jakarta.validation.constraints.*;
/**
 * MovieRequestDTO
 */
public record MovieRequest (
    @NotBlank String title,
    @NotNull Date release_date,
    @Min(1) Integer duration,
    @Min(0) @Max(10) Integer score,
    @NotNull Integer genre_id
) {}
