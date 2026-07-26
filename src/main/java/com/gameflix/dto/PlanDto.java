package com.gameflix.dto;

import java.math.BigDecimal;

public class PlanDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private int maxDevices;
    private boolean downloadAllowed;
    private String description;

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getMaxDevices() {
        return maxDevices;
    }

    public void setMaxDevices(int maxDevices) {
        this.maxDevices = maxDevices;
    }

    public boolean isDownloadAllowed() {
        return downloadAllowed;
    }

    public void setDownloadAllowed(boolean downloadAllowed) {
        this.downloadAllowed = downloadAllowed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
