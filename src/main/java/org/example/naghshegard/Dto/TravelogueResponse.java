package org.example.naghshegard.Dto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TravelogueResponse {
    private Long id;
    private String title;
    private String content;
    private String coverImageUrl;
    private String authorUsername;
    private List<String> visitedPlaceNames = new ArrayList<>();
    private List<Long> visitedPlaceIds = new ArrayList<>();
    private List<String> photoUrls = new ArrayList<>();
    private int viewCount;
    private int likeCount;
    private boolean approved;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TravelogueResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public String getAuthorUsername() { return authorUsername; }
    public void setAuthorUsername(String authorUsername) { this.authorUsername = authorUsername; }

    public List<String> getVisitedPlaceNames() { return visitedPlaceNames; }
    public void setVisitedPlaceNames(List<String> visitedPlaceNames) { this.visitedPlaceNames = visitedPlaceNames; }

    public List<Long> getVisitedPlaceIds() { return visitedPlaceIds; }
    public void setVisitedPlaceIds(List<Long> visitedPlaceIds) { this.visitedPlaceIds = visitedPlaceIds; }

    public List<String> getPhotoUrls() { return photoUrls; }
    public void setPhotoUrls(List<String> photoUrls) { this.photoUrls = photoUrls; }

    public int getViewCount() { return viewCount; }
    public void setViewCount(int viewCount) { this.viewCount = viewCount; }

    public int getLikeCount() { return likeCount; }
    public void setLikeCount(int likeCount) { this.likeCount = likeCount; }

    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
