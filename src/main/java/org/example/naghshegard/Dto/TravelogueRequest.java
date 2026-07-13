package org.example.naghshegard.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class TravelogueRequest {

    @NotBlank
    @Size(min = 3, max = 100)
    private String title;

    @NotBlank
    @Size(min = 10, max = 10000)
    private String content;

    private List<Long> visitedPlaceIds;

    private List<String> photoBase64List;

    public TravelogueRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<Long> getVisitedPlaceIds() { return visitedPlaceIds; }
    public void setVisitedPlaceIds(List<Long> visitedPlaceIds) { this.visitedPlaceIds = visitedPlaceIds; }

    public List<String> getPhotoBase64List() { return photoBase64List; }
    public void setPhotoBase64List(List<String> photoBase64List) { this.photoBase64List = photoBase64List; }
}