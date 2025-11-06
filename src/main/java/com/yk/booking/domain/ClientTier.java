package com.yk.booking.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A ClientTier.
 */
@Entity
@Table(name = "client_tier")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClientTier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tier_name", nullable = false)
    private String tierName;

    @Column(name = "discount_percentage", precision = 21, scale = 2)
    private BigDecimal discountPercentage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClientTier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTierName() {
        return this.tierName;
    }

    public ClientTier tierName(String tierName) {
        this.setTierName(tierName);
        return this;
    }

    public void setTierName(String tierName) {
        this.tierName = tierName;
    }

    public BigDecimal getDiscountPercentage() {
        return this.discountPercentage;
    }

    public ClientTier discountPercentage(BigDecimal discountPercentage) {
        this.setDiscountPercentage(discountPercentage);
        return this;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientTier)) {
            return false;
        }
        return getId() != null && getId().equals(((ClientTier) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientTier{" +
            "id=" + getId() +
            ", tierName='" + getTierName() + "'" +
            ", discountPercentage=" + getDiscountPercentage() +
            "}";
    }
}
