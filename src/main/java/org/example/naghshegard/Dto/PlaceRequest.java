package org.example.naghshegard.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class PlaceRequest {

    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private String googleMapsLink;

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotBlank String getDescription() {
        return description;
    }

    public void setGoogleMapsLink(@NotBlank String googleMapsLink) {
        this.googleMapsLink = googleMapsLink;
    }

    public String getGoogleMapsLink() {
        return googleMapsLink;
    }

    public void setDescription(@NotBlank String description) {
        this.description = description;
    }

    public @NotNull Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NotNull Double latitude) {
        this.latitude = latitude;
    }

    public @NotNull Double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NotNull Double longitude) {
        this.longitude = longitude;
    }
}