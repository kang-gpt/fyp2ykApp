package com.yk.booking.service.dto;

import java.math.BigDecimal;

public class RevenueDTO {

    private String name;
    private BigDecimal revenue;

    public RevenueDTO(String name, BigDecimal revenue) {
        this.name = name;
        this.revenue = revenue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}
