package org.example.naghshegard.Service;

import org.example.naghshegard.Dto.PlaceRequest;
import org.example.naghshegard.Dto.PlaceResponse;
import org.example.naghshegard.Model.Photos;
import org.example.naghshegard.Model.PlaceStatus;
import org.example.naghshegard.Model.TouristPlace;
import org.example.naghshegard.Model.User;
import org.example.naghshegard.Repository.PhotoRepository;
import org.example.naghshegard.Repository.ReviewRepository;
import org.example.naghshegard.Repository.TouristPlaceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TouristPlaceService {

    private final TouristPlaceRepository placeRepository;
    private final PhotoRepository photoRepository;
    private final UserService userService;
    private final ReviewRepository reviewRepository;

    public TouristPlaceService(TouristPlaceRepository placeRepository,
                               PhotoRepository photoRepository,
                               UserService userService,
                               ReviewRepository reviewRepository) {
        this.placeRepository = placeRepository;
        this.photoRepository = photoRepository;
        this.userService = userService;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public TouristPlace createPlace(PlaceRequest request, String username, List<MultipartFile> images) {
        User user = userService.findByUsername(username);

        TouristPlace place = new TouristPlace();
        place.setName(request.getName());
        place.setDescription(request.getDescription());
        place.setGoogleMapsLink(request.getGoogleMapsLink());
        place.setUser(user);
        place.setStatus(PlaceStatus.PENDING);

        TouristPlace savedPlace = placeRepository.save(place);

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                try {
                    Photos photo = new Photos();
                    photo.setImageData(image.getBytes());
                    photo.setContentType(image.getContentType());
                    photo.setFileName(image.getOriginalFilename());
                    photo.setPlace(savedPlace);
                    photoRepository.save(photo);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image", e);
                }
            }
        }

        return savedPlace;
    }

    @Transactional
    public PlaceResponse getPlaceById(Long id) {
        TouristPlace place = placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + id));

        place.incrementViewCount();
        placeRepository.save(place);

        return convertToResponse(place);
    }

    public TouristPlace findById(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + id));
    }

    public Page<PlaceResponse> getApprovedPlaces(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return placeRepository.findByStatus(PlaceStatus.APPROVED, pageable).map(this::convertToResponse);
    }

    public Page<PlaceResponse> searchPlaces(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return placeRepository.searchApprovedPlaces(keyword, pageable).map(this::convertToResponse);
    }

    public List<PlaceResponse> getLatestPlaces(int limit) {
        List<TouristPlace> places = placeRepository.findLatestPlaces(PageRequest.of(0, limit));
        return places.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public List<PlaceResponse> getMostViewedPlaces(int limit) {
        List<TouristPlace> places = placeRepository.findMostViewedPlaces(PageRequest.of(0, limit));
        return places.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public List<PlaceResponse> getMostLikedPlaces(int limit) {
        List<TouristPlace> places = placeRepository.findMostLikedPlaces(PageRequest.of(0, limit));
        return places.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public List<PlaceResponse> getPlacesByUser(String username) {
        User user = userService.findByUsername(username);
        return placeRepository.findByUser(user).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void likePlace(Long id) {
        TouristPlace place = placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + id));
        place.setLikeCount(place.getLikeCount() + 1);
        placeRepository.save(place);
    }

    @Transactional
    public TouristPlace approvePlace(Long placeId) {
        TouristPlace place = findById(placeId);
        place.setStatus(PlaceStatus.APPROVED);
        return placeRepository.save(place);
    }

    @Transactional
    public TouristPlace rejectPlace(Long placeId) {
        TouristPlace place = findById(placeId);
        place.setStatus(PlaceStatus.REJECTED);
        return placeRepository.save(place);
    }

    private PlaceResponse convertToResponse(TouristPlace place) {
        PlaceResponse response = new PlaceResponse();
        response.setId(place.getId());
        response.setName(place.getName());
        response.setDescription(place.getDescription());
        response.setGoogleMapsLink(place.getGoogleMapsLink());
        response.setIntroducerUsername(place.getUser().getUsername());
        response.setViewCount(place.getViewCount());
        response.setLikeCount(place.getLikeCount());
        response.setCreatedAt(place.getCreatedAt());
        response.setStatus(place.getStatus().name());  // اضافه شد - مهم

        Double avgRating = placeRepository.getAverageRating(place.getId());
        response.setAverageRating(avgRating != null ? avgRating : 0.0);

        List<Photos> photos = photoRepository.findByPlace(place);
        List<String> photoUrls = new ArrayList<>();
        for (Photos photo : photos) {
            photoUrls.add("/api/places/photos/" + photo.getId());
        }
        response.setPhotoUrls(photoUrls);

        return response;
    }

    public long getTotalPlacesCount() {
        return placeRepository.count();
    }

    public long getPendingPlacesCount() {
        return placeRepository.countByStatus(PlaceStatus.PENDING);
    }

    public long getApprovedPlacesCount() {
        return placeRepository.countByStatus(PlaceStatus.APPROVED);
    }

    public List<PlaceResponse> getPendingPlaces() {
        return placeRepository.findByStatus(PlaceStatus.PENDING).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<PlaceResponse> getAllPlaces() {
        return placeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePlace(Long id) {
        System.out.println("Deleting place with id: " + id);

        TouristPlace place = placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + id));

        System.out.println("Found place: " + place.getName());
        System.out.println("Number of photos: " + place.getPhotos().size());
        System.out.println("Number of reviews: " + place.getReviews().size());

        // حذف عکس‌ها
        if (place.getPhotos() != null && !place.getPhotos().isEmpty()) {
            photoRepository.deleteAll(place.getPhotos());
            System.out.println("Photos deleted");
        }

        // حذف نظرات
        if (place.getReviews() != null && !place.getReviews().isEmpty()) {
            reviewRepository.deleteAll(place.getReviews());
            System.out.println("Reviews deleted");
        }

        // حذف مکان
        placeRepository.delete(place);
        System.out.println("Place deleted successfully");
    }

    public long getTotalPhotosCount() {
        return photoRepository.count();
    }

    public List<PlaceResponse> searchPlacesList(String keyword) {
        List<TouristPlace> places = placeRepository.searchApprovedPlacesList(keyword);
        return places.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<PlaceResponse> searchPlacesSimple(String keyword) {
        List<TouristPlace> places = placeRepository.searchAllPlaces(keyword);
        return places.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    @Transactional
    public void deletePlaceWithUserCheck(Long id, String username) {
        TouristPlace place = placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Place not found with id: " + id));

        User currentUser = userService.findByUsername(username);
        boolean isAdmin = currentUser.getRole().name().equals("ROLE_ADMIN");

        if (!place.getUser().getId().equals(currentUser.getId()) && !isAdmin) {
            throw new RuntimeException("You don't have permission to delete this place");
        }

        deletePlace(id);
    }
}