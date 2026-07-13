package org.example.naghshegard.Controller;

import org.example.naghshegard.Dto.TravelogueRequest;
import org.example.naghshegard.Dto.TravelogueResponse;
import org.example.naghshegard.Jwt.JwtUtil;
import org.example.naghshegard.Model.Photos;
import org.example.naghshegard.Service.PhotoService;
import org.example.naghshegard.Service.TravelogueService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/travelogues")
public class TravelogueController {

    private final TravelogueService travelogueService;
    private final PhotoService photoService;
    private final JwtUtil jwtUtil;

    public TravelogueController(TravelogueService travelogueService,
                                PhotoService photoService,
                                JwtUtil jwtUtil) {
        this.travelogueService = travelogueService;
        this.photoService = photoService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/photos/{photoId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long photoId) {
        Photos photo = photoService.getPhotoById(photoId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(photo.getContentType()))
                .body(photo.getImageData());
    }

    @GetMapping("/public")
    public ResponseEntity<Page<TravelogueResponse>> getApprovedTravelogues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(travelogueService.getApprovedTravelogues(page, size));
    }

    @GetMapping("/public/latest")
    public ResponseEntity<List<TravelogueResponse>> getLatestTravelogues(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(travelogueService.getLatestTravelogues(limit));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<TravelogueResponse> getTravelogueById(@PathVariable Long id) {
        return ResponseEntity.ok(travelogueService.getTravelogueById(id));
    }

    @GetMapping("/my-travelogues")
    public ResponseEntity<List<TravelogueResponse>> getMyTravelogues() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return ResponseEntity.ok(travelogueService.getTraveloguesByUser(username));
    }

    @PostMapping(value = "/add", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createTravelogue(
            @RequestPart("travelogue") TravelogueRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String username = authentication.getName();
        travelogueService.createTravelogue(request, username, images);
        return ResponseEntity.status(HttpStatus.CREATED).body("Travelogue created successfully");
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likeTravelogue(@PathVariable Long id) {
        travelogueService.likeTravelogue(id);
        return ResponseEntity.ok("Liked successfully");
    }

    @PutMapping("/admin/{id}/approve")
    public ResponseEntity<?> approveTravelogue(@PathVariable Long id) {
        travelogueService.approveTravelogue(id);
        return ResponseEntity.ok("Travelogue approved");
    }

    @GetMapping("/admin/pending")
    public ResponseEntity<List<TravelogueResponse>> getPendingTravelogues() {
        return ResponseEntity.ok(travelogueService.getPendingTravelogues());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTravelogue(@PathVariable Long id, Authentication authentication) {
        try {
            String username = authentication.getName();
            travelogueService.deleteTravelogueWithUserCheck(id, username);
            return ResponseEntity.ok(Map.of("message", "Travelogue deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete travelogue: " + e.getMessage()));
        }
    }

    @GetMapping("/public/search")
    public ResponseEntity<Page<TravelogueResponse>> searchTravelogues(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(travelogueService.searchTravelogues(keyword, page, size));
    }

    @GetMapping("/public/most-viewed")
    public ResponseEntity<List<TravelogueResponse>> getMostViewed(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(travelogueService.getMostViewedTravelogues(limit));
    }

    @GetMapping("/public/most-liked")
    public ResponseEntity<List<TravelogueResponse>> getMostLiked(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(travelogueService.getMostLikedTravelogues(limit));
    }
}