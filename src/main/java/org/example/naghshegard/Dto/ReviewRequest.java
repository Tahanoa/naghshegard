package org.example.naghshegard.Dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
public class ReviewRequest {
    private Long placeId;

    private String comment;

    private Integer rating;

    public @NotNull Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(@NotNull Long placeId) {
        this.placeId = placeId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public @NotNull @Min(1) @Max(5) Integer getRating() {
        return rating;
    }

    public void setRating(@NotNull @Min(1) @Max(5) Integer rating) {
        this.rating = rating;
    }
}
