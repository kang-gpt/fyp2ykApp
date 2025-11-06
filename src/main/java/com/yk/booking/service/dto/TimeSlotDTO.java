package com.yk.booking.service.dto;

import com.yk.booking.service.dto.CourtDTO;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.yk.booking.domain.TimeSlot} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TimeSlotDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant startTime;

    @NotNull
    private Instant endTime;

    private CourtDTO court;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public CourtDTO getCourt() {
        return court;
    }

    public void setCourt(CourtDTO court) {
        this.court = court;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeSlotDTO)) {
            return false;
        }

        TimeSlotDTO timeSlotDTO = (TimeSlotDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timeSlotDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeSlotDTO{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", court=" + getCourt() +
            "}";
    }
}
