package com.yk.booking.service.dto;

import com.yk.booking.domain.enumeration.ClientTier;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.yk.booking.domain.TierVoucher} entity.
 */
public class TierVoucherDTO implements Serializable {

    private Long id;

    @NotNull
    private ClientTier tier;

    @NotNull
    private String voucherType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClientTier getTier() {
        return tier;
    }

    public void setTier(ClientTier tier) {
        this.tier = tier;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TierVoucherDTO)) {
            return false;
        }

        TierVoucherDTO tierVoucherDTO = (TierVoucherDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tierVoucherDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "TierVoucherDTO{" + "id=" + getId() + ", tier='" + getTier() + "'" + ", voucherType='" + getVoucherType() + "'" + "}";
    }
}
