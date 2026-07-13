package org.example.naghshegard.Service;


import org.example.naghshegard.Dto.ReviewRequest;
import org.example.naghshegard.Dto.ReviewResponse;
import org.example.naghshegard.Model.Review;
import org.example.naghshegard.Model.TouristPlace;
import org.example.naghshegard.Model.User;
import org.example.naghshegard.Repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TouristPlaceService placeService;
    private final UserService userService;

    public ReviewService(ReviewRepository reviewRepository,
                         TouristPlaceService placeService,
                         UserService userService) {
        this.reviewRepository = reviewRepository;
        this.placeService = placeService;
        this.userService = userService;
    }

    @Transactional
    public Review addReview(ReviewRequest request, String username) {
        User user = userService.findByUsername(username);
        TouristPlace place = placeService.findById(request.getPlaceId());

        if (reviewRepository.existsByUserAndPlace(user, place)) {
            throw new RuntimeException("You have already reviewed this place");
        }

        Review review = new Review();
        review.setComment(request.getComment());
        review.setRating(request.getRating());
        review.setUser(user);
        review.setPlace(place);
        review.setApproved(false);

        return reviewRepository.save(review);
    }

    @Transactional
    public Review approveReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setApproved(true);
        return reviewRepository.save(review);
    }

    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getPendingReviews() {
        List<Review> reviews = reviewRepository.findByApprovedFalse();
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> getApprovedReviewsByPlace(Long placeId) {
        List<Review> reviews = reviewRepository.findByPlaceIdAndApproved(placeId, true);
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public long getTotalReviewsCount() {
        return reviewRepository.count();
    }

    public long getPendingReviewsCount() {
        return reviewRepository.countByApprovedFalse();
    }

    private ReviewResponse convertToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setComment(review.getComment());
        response.setRating(review.getRating());
        response.setUsername(review.getUser().getUsername());
        response.setApproved(review.isApproved());  // اضافه شد - مهم
        response.setCreatedAt(review.getCreatedAt());

        // اضافه کردن نام مکان
        if (review.getPlace() != null) {
            response.setPlaceName(review.getPlace().getName());
        }

        return response;
    }

    public List<ReviewResponse> searchReviews(String keyword) {
        List<Review> reviews = reviewRepository.searchReviews(keyword);
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteReviewWithUserCheck(Long id, String username) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        User currentUser = userService.findByUsername(username);
        boolean isAdmin = currentUser.getRole().name().equals("ROLE_ADMIN");

        if (!review.getUser().getId().equals(currentUser.getId()) && !isAdmin) {
            throw new RuntimeException("You don't have permission to delete this review");
        }

        reviewRepository.delete(review);
    }
}