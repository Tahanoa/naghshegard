package org.example.naghshegard.Dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlaceResponse {
    private Long id;
    private String name;
    private String description;
    private String googleMapsLink;
    private Double latitude;
    private Double longitude;
    private String introducerUsername;
    private Double averageRating;
    private int viewCount;
    private int likeCount;
    private LocalDateTime createdAt;
    private String status;  // اضافه شد - مهم
    private List<String> photoUrls = new ArrayList<>();

    // Constructors
    public PlaceResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGoogleMapsLink() { return googleMapsLink; }
    public void setGoogleMapsLink(String googleMapsLink) { this.googleMapsLink = googleMapsLink; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getIntroducerUsername() { return introducerUsername; }
    public void setIntroducerUsername(String introducerUsername) { this.introducerUsername = introducerUsername; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }

    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getPhotoUrls() { return photoUrls; }
    public void setPhotoUrls(List<String> photoUrls) { this.photoUrls = photoUrls; }
}