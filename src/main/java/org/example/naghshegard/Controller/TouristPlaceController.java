package org.example.naghshegard.Controller;


import org.example.naghshegard.Dto.PlaceRequest;
import org.example.naghshegard.Dto.PlaceResponse;
import org.example.naghshegard.Model.Photos;
import org.example.naghshegard.Service.PhotoService;
import org.example.naghshegard.Service.TouristPlaceService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/places")
public class TouristPlaceController {

    private final TouristPlaceService placeService;
    private final PhotoService photoService;

    public TouristPlaceController(TouristPlaceService placeService, PhotoService photoService) {
        this.placeService = placeService;
        this.photoService = photoService;
    }

    @GetMapping("/public")
    public ResponseEntity<Page<PlaceResponse>> getApprovedPlaces(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        return ResponseEntity.ok(placeService.getApprovedPlaces(page, size));
    }

    @GetMapping("/public/latest")
    public ResponseEntity<List<PlaceResponse>> getLatestPlaces(@RequestParam(defaultValue = "6") int limit) {
        return ResponseEntity.ok(placeService.getLatestPlaces(limit));
    }

    @GetMapping("/public/most-viewed")
    public ResponseEntity<List<PlaceResponse>> getMostViewedPlaces(@RequestParam(defaultValue = "6") int limit) {
        return ResponseEntity.ok(placeService.getMostViewedPlaces(limit));
    }

    @GetMapping("/public/most-liked")
    public ResponseEntity<List<PlaceResponse>> getMostLikedPlaces(@RequestParam(defaultValue = "6") int limit) {
        return ResponseEntity.ok(placeService.getMostLikedPlaces(limit));
    }

    @GetMapping("/public/search")
    public ResponseEntity<Page<PlaceResponse>> searchPlaces(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        return ResponseEntity.ok(placeService.searchPlaces(keyword, page, size));
    }

    @GetMapping("/my-places")
    public ResponseEntity<List<PlaceResponse>> getMyPlaces(Authentication authentication) {
        return ResponseEntity.ok(placeService.getPlacesByUser(authentication.getName()));
    }

    @PostMapping(value = "/add", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addPlace(
            @RequestPart("place") PlaceRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            Authentication authentication) {

        placeService.createPlace(request, authentication.getName(), images);
        return ResponseEntity.status(HttpStatus.CREATED).body("Place added successfully. Waiting for admin approval.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponse> getPlaceById(@PathVariable Long id) {
        return ResponseEntity.ok(placeService.getPlaceById(id));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePlace(@PathVariable Long id) {
        placeService.likePlace(id);
        return ResponseEntity.ok("Liked successfully");
    }

    @GetMapping("/photos/{photoId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long photoId) {
        Photos photo = photoService.getPhotoById(photoId);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(photo.getContentType()))
                .body(photo.getImageData());
    }

    @PutMapping("/admin/{id}/approve")
    public ResponseEntity<?> approvePlace(@PathVariable Long id) {
        placeService.approvePlace(id);
        return ResponseEntity.ok("Place approved");
    }

    @PutMapping("/admin/{id}/reject")
    public ResponseEntity<?> rejectPlace(@PathVariable Long id) {
        placeService.rejectPlace(id);
        return ResponseEntity.ok("Place rejected");
    }

    @GetMapping("/public/search-list")
    public ResponseEntity<List<PlaceResponse>> searchPlacesList(@RequestParam String keyword) {
        List<PlaceResponse> places = placeService.searchPlacesList(keyword);
        return ResponseEntity.ok(places);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable Long id, Authentication authentication) {
        try {
            String username = authentication.getName();
            placeService.deletePlaceWithUserCheck(id, username);
            return ResponseEntity.ok(Map.of("message", "Place deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete place: " + e.getMessage()));
        }
    }
}