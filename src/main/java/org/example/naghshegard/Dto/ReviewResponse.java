package org.example.naghshegard.Dto;

import java.time.LocalDateTime;

public class ReviewResponse {
    private Long id;
    private String comment;
    private Integer rating;
    private String username;
    private String placeName;  // مهم
    private boolean approved;  // مهم
    private LocalDateTime createdAt;

    public ReviewResponse() {}

    public Long getId() { return id; }
    public String getComment() { return comment; }
    public Integer getRating() { return rating; }
    public String getUsername() { return username; }
    public String getPlaceName() { return placeName; }
    public boolean isApproved() { return approved; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setId(Long id) { this.id = id; }
    public void setComment(String comment) { this.comment = comment; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setUsername(String username) { this.username = username; }
    public void setPlaceName(String placeName) { this.placeName = placeName; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}