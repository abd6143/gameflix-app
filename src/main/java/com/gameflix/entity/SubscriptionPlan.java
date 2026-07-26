package com.gameflix.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int maxDevices;

    @Column(nullable = false)
    private boolean downloadAllowed;

    @Column(nullable = false, length = 500)
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
