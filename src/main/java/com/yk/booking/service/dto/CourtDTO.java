package com.yk.booking.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.yk.booking.domain.Court} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourtDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private SportDTO sport;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SportDTO getSport() {
        return sport;
    }

    public void setSport(SportDTO sport) {
        this.sport = sport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourtDTO)) {
            return false;
        }

        CourtDTO courtDTO = (CourtDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, courtDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourtDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", sport=" + getSport() +
            "}";
    }
}
