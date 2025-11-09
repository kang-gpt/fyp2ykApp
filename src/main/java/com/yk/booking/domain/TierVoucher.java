package com.yk.booking.domain;

import com.yk.booking.domain.enumeration.ClientTier;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A TierVoucher - maps client tiers to their assigned vouchers.
 */
@Entity
@Table(name = "tier_voucher")
public class TierVoucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tier", nullable = false, unique = true)
    private ClientTier tier;

    @NotNull
    @Column(name = "voucher_type", nullable = false)
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
        if (!(o instanceof TierVoucher)) {
            return false;
        }
        return id != null && id.equals(((TierVoucher) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "TierVoucher{" + "id=" + getId() + ", tier='" + getTier() + "'" + ", voucherType='" + getVoucherType() + "'" + "}";
    }
}
