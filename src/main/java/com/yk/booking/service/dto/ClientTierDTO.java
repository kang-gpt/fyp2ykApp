package com.yk.booking.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.yk.booking.domain.ClientTier} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientTierDTO implements Serializable {

    private Long id;

    @NotNull
    private String tierName;

    private BigDecimal discountPercentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTierName() {
        return tierName;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientTierDTO)) {
            return false;
        }

        ClientTierDTO clientTierDTO = (ClientTierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientTierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientTierDTO{" +
            "id=" + getId() +
            ", tierName='" + getTierName() + "'" +
            ", discountPercentage=" + getDiscountPercentage() +
            "}";
    }
}
