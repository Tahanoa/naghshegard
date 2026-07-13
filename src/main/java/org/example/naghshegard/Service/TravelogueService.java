package org.example.naghshegard.Service;

import org.example.naghshegard.Dto.TravelogueRequest;
import org.example.naghshegard.Dto.TravelogueResponse;
import org.example.naghshegard.Model.Photos;
import org.example.naghshegard.Model.Travelogue;
import org.example.naghshegard.Model.User;
import org.example.naghshegard.Repository.PhotoRepository;
import org.example.naghshegard.Repository.TravelogueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelogueService {

    private final TravelogueRepository travelogueRepository;
    private final PhotoRepository photoRepository;
    private final UserService userService;
    private final TouristPlaceService placeService;

    public TravelogueService(TravelogueRepository travelogueRepository,
                             PhotoRepository photoRepository,
                             UserService userService,
                             TouristPlaceService placeService) {
        this.travelogueRepository = travelogueRepository;
        this.photoRepository = photoRepository;
        this.userService = userService;
        this.placeService = placeService;
    }

    @Transactional
    public Travelogue createTravelogue(TravelogueRequest request, String username, List<MultipartFile> images) {
        User user = userService.findByUsername(username);

        Travelogue travelogue = new Travelogue();
        travelogue.setTitle(request.getTitle());
        travelogue.setContent(request.getContent());
        travelogue.setUser(user);
        travelogue.setApproved(false);
        travelogue.setCreatedAt(LocalDateTime.now());
        travelogue.setUpdatedAt(LocalDateTime.now());

        if (request.getVisitedPlaceIds() != null && !request.getVisitedPlaceIds().isEmpty()) {
            String visitedPlacesStr = request.getVisitedPlaceIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            travelogue.setVisitedPlaces(visitedPlacesStr);
        }

        Travelogue savedTravelogue = travelogueRepository.save(travelogue);

        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                try {
                    Photos photo = new Photos();
                    photo.setImageData(image.getBytes());
                    photo.setContentType(image.getContentType());
                    photo.setFileName(image.getOriginalFilename());
                    photo.setTravelogue(savedTravelogue);
                    photoRepository.save(photo);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image", e);
                }
            }
        }

        return savedTravelogue;
    }

    @Transactional
    public TravelogueResponse getTravelogueById(Long id) {
        Travelogue travelogue = travelogueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travelogue not found"));

        travelogue.incrementViewCount();
        travelogue.setUpdatedAt(LocalDateTime.now());
        travelogueRepository.save(travelogue);

        return convertToResponse(travelogue);
    }

    public Page<TravelogueResponse> getApprovedTravelogues(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return travelogueRepository.findByApprovedTrue(pageable).map(this::convertToResponse);
    }

    public Page<TravelogueResponse> searchTravelogues(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return travelogueRepository.searchApprovedTravelogues(keyword, pageable).map(this::convertToResponse);
    }

    public List<TravelogueResponse> getLatestTravelogues(int limit) {
        List<Travelogue> travelogues = travelogueRepository.findLatestTravelogues(PageRequest.of(0, limit));
        return travelogues.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<TravelogueResponse> getMostViewedTravelogues(int limit) {
        List<Travelogue> travelogues = travelogueRepository.findMostViewedTravelogues(PageRequest.of(0, limit));
        return travelogues.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<TravelogueResponse> getMostLikedTravelogues(int limit) {
        List<Travelogue> travelogues = travelogueRepository.findMostLikedTravelogues(PageRequest.of(0, limit));
        return travelogues.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<TravelogueResponse> getTraveloguesByUser(String username) {
        User user = userService.findByUsername(username);
        return travelogueRepository.findByUser(user).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<TravelogueResponse> getApprovedTraveloguesByUser(String username) {
        User user = userService.findByUsername(username);
        return travelogueRepository.findByUserAndApproved(user, true).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<TravelogueResponse> getPendingTravelogues() {
        return travelogueRepository.findByApprovedFalse().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveTravelogue(Long id) {
        Travelogue travelogue = travelogueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travelogue not found"));
        travelogue.setApproved(true);
        travelogue.setUpdatedAt(LocalDateTime.now());
        travelogueRepository.save(travelogue);
    }

    @Transactional
    public void likeTravelogue(Long id) {
        Travelogue travelogue = travelogueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travelogue not found"));
        travelogue.incrementLikeCount();
        travelogue.setUpdatedAt(LocalDateTime.now());
        travelogueRepository.save(travelogue);
    }

    @Transactional
    public void deleteTravelogue(Long id) {
        Travelogue travelogue = travelogueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travelogue not found with id: " + id));

        System.out.println("Deleting travelogue: " + travelogue.getTitle());

        // حذف عکس‌های مرتبط
        List<Photos> photos = photoRepository.findByTravelogue(travelogue);
        if (photos != null && !photos.isEmpty()) {
            photoRepository.deleteAll(photos);
            System.out.println("Deleted " + photos.size() + " photos");
        }

        // حذف سفرنامه
        travelogueRepository.delete(travelogue);
        System.out.println("Travelogue deleted successfully");
    }

    @Transactional
    public void deleteTravelogueWithUserCheck(Long id, String username) {
        Travelogue travelogue = travelogueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Travelogue not found"));

        User currentUser = userService.findByUsername(username);
        boolean isAdmin = currentUser.getRole().name().equals("ROLE_ADMIN");

        if (!travelogue.getUser().getId().equals(currentUser.getId()) && !isAdmin) {
            throw new RuntimeException("You don't have permission to delete this travelogue");
        }

        deleteTravelogue(id);
    }

    private TravelogueResponse convertToResponse(Travelogue travelogue) {
        TravelogueResponse response = new TravelogueResponse();
        response.setId(travelogue.getId());
        response.setTitle(travelogue.getTitle());
        response.setContent(travelogue.getContent());
        response.setAuthorUsername(travelogue.getUser().getUsername());
        response.setViewCount(travelogue.getViewCount());
        response.setLikeCount(travelogue.getLikeCount());
        response.setApproved(travelogue.isApproved());
        response.setCreatedAt(travelogue.getCreatedAt());
        response.setUpdatedAt(travelogue.getUpdatedAt());
        List<Photos> photos = photoRepository.findByTravelogue(travelogue);
        List<String> photoUrls = photos.stream()
                .map(p -> "/api/travelogues/photos/" + p.getId())
                .collect(Collectors.toList());
        response.setPhotoUrls(photoUrls);

        if (!photoUrls.isEmpty()) {
            response.setCoverImageUrl(photoUrls.get(0));
        }

        if (travelogue.getVisitedPlaces() != null && !travelogue.getVisitedPlaces().isEmpty()) {
            String[] placeIds = travelogue.getVisitedPlaces().split(",");
            List<Long> ids = new ArrayList<>();
            List<String> names = new ArrayList<>();

            for (String idStr : placeIds) {
                try {
                    Long placeId = Long.parseLong(idStr.trim());
                    ids.add(placeId);
                    try {
                        names.add(placeService.findById(placeId).getName());
                    } catch (Exception e) {
                        names.add("مکان نامشخص");
                    }
                } catch (NumberFormatException e) {
                }
            }
            response.setVisitedPlaceIds(ids);
            response.setVisitedPlaceNames(names);
        }

        return response;
    }

    public long getTotalTraveloguesCount() {
        return travelogueRepository.count();
    }

    public long getPendingTraveloguesCount() {
        return travelogueRepository.countByApprovedFalse();
    }

    public long getApprovedTraveloguesCount() {
        return travelogueRepository.countByApprovedTrue();
    }

    public List<TravelogueResponse> getAllTravelogues() {
        return travelogueRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    public List<TravelogueResponse> searchTraveloguesSimple(String keyword) {
        List<Travelogue> travelogues = travelogueRepository.searchAllTravelogues(keyword);
        return travelogues.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
}