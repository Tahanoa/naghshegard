package org.example.naghshegard.Controller;


import org.example.naghshegard.Dto.PlaceResponse;
import org.example.naghshegard.Dto.ReviewResponse;
import org.example.naghshegard.Dto.TravelogueResponse;
import org.example.naghshegard.Dto.UserResponse;
import org.example.naghshegard.Service.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
public class AdminController {

    private final TouristPlaceService placeService;
    private final ReviewService reviewService;
    private final TravelogueService travelogueService;
    private final UserService userService;
    private final VerificationService verificationService;

    public AdminController(TouristPlaceService placeService,
                           ReviewService reviewService,
                           TravelogueService travelogueService,
                           UserService userService,
                           VerificationService verificationService) {
        this.placeService = placeService;
        this.reviewService = reviewService;
        this.travelogueService = travelogueService;
        this.userService = userService;
        this.verificationService = verificationService;
    }

    // ====== Dashboard Stats ======
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPlaces", placeService.getTotalPlacesCount());
        stats.put("pendingPlaces", placeService.getPendingPlacesCount());
        stats.put("approvedPlaces", placeService.getApprovedPlacesCount());
        stats.put("totalTravelogues", travelogueService.getTotalTraveloguesCount());
        stats.put("pendingTravelogues", travelogueService.getPendingTraveloguesCount());
        stats.put("approvedTravelogues", travelogueService.getApprovedTraveloguesCount());
        stats.put("totalReviews", reviewService.getTotalReviewsCount());
        stats.put("pendingReviews", reviewService.getPendingReviewsCount());
        stats.put("totalUsers", userService.getTotalUsersCount());
        stats.put("totalPhotos", placeService.getTotalPhotosCount());

        // اضافه کردن آمار جدید
        stats.put("totalPending", placeService.getPendingPlacesCount() +
                travelogueService.getPendingTraveloguesCount() +
                reviewService.getPendingReviewsCount());
        return ResponseEntity.ok(stats);
    }

    // ====== Places Management ======
    @GetMapping("/places/pending")
    public ResponseEntity<List<PlaceResponse>> getPendingPlaces() {
        return ResponseEntity.ok(placeService.getPendingPlaces());
    }

    @GetMapping("/places/all")
    public ResponseEntity<List<PlaceResponse>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @GetMapping("/places/search")
    public ResponseEntity<List<PlaceResponse>> searchPlaces(@RequestParam String keyword) {
        return ResponseEntity.ok(placeService.searchPlacesSimple(keyword));
    }

    @PutMapping("/places/{id}/approve")
    public ResponseEntity<?> approvePlace(@PathVariable Long id) {
        placeService.approvePlace(id);
        return ResponseEntity.ok(Map.of("message", "Place approved successfully"));
    }

    @PutMapping("/places/{id}/reject")
    public ResponseEntity<?> rejectPlace(@PathVariable Long id) {
        placeService.rejectPlace(id);
        return ResponseEntity.ok(Map.of("message", "Place rejected successfully"));
    }

    @GetMapping("/travelogues/pending")
    public ResponseEntity<List<TravelogueResponse>> getPendingTravelogues() {
        return ResponseEntity.ok(travelogueService.getPendingTravelogues());
    }

    @GetMapping("/travelogues/all")
    public ResponseEntity<List<TravelogueResponse>> getAllTravelogues() {
        return ResponseEntity.ok(travelogueService.getAllTravelogues());
    }

    @GetMapping("/travelogues/search")
    public ResponseEntity<List<TravelogueResponse>> searchTravelogues(@RequestParam String keyword) {
        return ResponseEntity.ok(travelogueService.searchTraveloguesSimple(keyword));
    }

    @PutMapping("/travelogues/{id}/approve")
    public ResponseEntity<?> approveTravelogue(@PathVariable Long id) {
        travelogueService.approveTravelogue(id);
        return ResponseEntity.ok(Map.of("message", "Travelogue approved successfully"));
    }

    @GetMapping("/reviews/pending")
    public ResponseEntity<List<ReviewResponse>> getPendingReviews() {
        return ResponseEntity.ok(reviewService.getPendingReviews());
    }

    @GetMapping("/reviews/all")
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/reviews/search")
    public ResponseEntity<List<ReviewResponse>> searchReviews(@RequestParam String keyword) {
        return ResponseEntity.ok(reviewService.searchReviews(keyword));
    }

    @PutMapping("/reviews/{id}/approve")
    public ResponseEntity<?> approveReview(@PathVariable Long id) {
        reviewService.approveReview(id);
        return ResponseEntity.ok(Map.of("message", "Review approved successfully"));
    }

    // ====== Users Management ======
    @GetMapping("/users/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String keyword) {
        return ResponseEntity.ok(userService.searchUsers(keyword));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id, @RequestParam String role) {
        userService.changeUserRole(id, role);
        return ResponseEntity.ok(Map.of("message", "User role updated successfully"));
    }

    @PutMapping("/users/{id}/toggle-status")
    public ResponseEntity<?> toggleUserStatus(@PathVariable Long id) {
        userService.toggleUserStatus(id);
        return ResponseEntity.ok(Map.of("message", "User status toggled successfully"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    // ====== Bulk Actions ======
    @PutMapping("/places/bulk-approve")
    public ResponseEntity<?> bulkApprovePlaces(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            placeService.approvePlace(id);
        }
        return ResponseEntity.ok(Map.of("message", "Places approved successfully"));
    }

    @PutMapping("/travelogues/bulk-approve")
    public ResponseEntity<?> bulkApproveTravelogues(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            travelogueService.approveTravelogue(id);
        }
        return ResponseEntity.ok(Map.of("message", "Travelogues approved successfully"));
    }

    @PutMapping("/reviews/bulk-approve")
    public ResponseEntity<?> bulkApproveReviews(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            reviewService.approveReview(id);
        }
        return ResponseEntity.ok(Map.of("message", "Reviews approved successfully"));
    }

    @GetMapping("/verification-codes")
    public ResponseEntity<?> getVerificationCodes() {
        return ResponseEntity.ok(verificationService.getAllCodes());
    }

    @DeleteMapping("/verification-codes/{id}")
    public ResponseEntity<?> deleteVerificationCode(@PathVariable Long id) {
        verificationService.deleteCode(id);
        return ResponseEntity.ok(Map.of("message", "Code deleted successfully"));
    }

    @DeleteMapping("/verification-codes/cleanup")
    public ResponseEntity<?> cleanupExpiredCodes() {
        verificationService.cleanupExpiredCodes();
        return ResponseEntity.ok(Map.of("message", "Expired codes cleaned up"));
    }

    @DeleteMapping("/places/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable Long id) {
        try {
            placeService.deletePlace(id);
            return ResponseEntity.ok(Map.of("message", "Place deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete place: " + e.getMessage()));
        }
    }

    @DeleteMapping("/travelogues/{id}")
    public ResponseEntity<?> deleteTravelogue(@PathVariable Long id) {
        try {
            travelogueService.deleteTravelogue(id);
            return ResponseEntity.ok(Map.of("message", "Travelogue deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete travelogue: " + e.getMessage()));
        }
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok(Map.of("message", "Review deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete review: " + e.getMessage()));
        }
    }
}