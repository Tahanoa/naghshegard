package org.example.naghshegard.Controller;


import org.example.naghshegard.Dto.ReviewRequest;
import org.example.naghshegard.Dto.ReviewResponse;
import org.example.naghshegard.Service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody ReviewRequest request) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            reviewService.addReview(request, username);
            return ResponseEntity.status(HttpStatus.CREATED).body("Review added successfully. Waiting for admin approval.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/place/{placeId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByPlace(@PathVariable Long placeId) {
        List<ReviewResponse> reviews = reviewService.getApprovedReviewsByPlace(placeId);
        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id, Authentication authentication) {
        try {
            String username = authentication.getName();
            reviewService.deleteReviewWithUserCheck(id, username);
            return ResponseEntity.ok(Map.of("message", "Review deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete review: " + e.getMessage()));
        }
    }

}