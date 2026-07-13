package org.example.naghshegard.Repository;


import org.example.naghshegard.Model.PlaceStatus;
import org.example.naghshegard.Model.TouristPlace;
import org.example.naghshegard.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TouristPlaceRepository extends JpaRepository<TouristPlace, Long> {

    List<TouristPlace> findByStatus(PlaceStatus status);
    Page<TouristPlace> findByStatus(PlaceStatus status, Pageable pageable);

    List<TouristPlace> findByUser(User user);

    List<TouristPlace> findByStatusAndUser(PlaceStatus status, User user);

    @Query("SELECT p FROM TouristPlace p WHERE p.status = 'APPROVED' AND " +
            "(p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    Page<TouristPlace> searchApprovedPlaces(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.place.id = :placeId AND r.approved = true")
    Double getAverageRating(@Param("placeId") Long placeId);

    @Query("SELECT p FROM TouristPlace p WHERE p.status = 'APPROVED' ORDER BY p.viewCount DESC")
    List<TouristPlace> findMostViewedPlaces(Pageable pageable);

    @Query("SELECT p FROM TouristPlace p WHERE p.status = 'APPROVED' ORDER BY p.likeCount DESC")
    List<TouristPlace> findMostLikedPlaces(Pageable pageable);

    @Query("SELECT p FROM TouristPlace p WHERE p.status = 'APPROVED' ORDER BY p.createdAt DESC")
    List<TouristPlace> findLatestPlaces(Pageable pageable);

    long countByStatus(PlaceStatus status);

    @Query("SELECT p FROM TouristPlace p WHERE p.status = 'APPROVED' AND " +
            "(p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
    List<TouristPlace> searchApprovedPlacesList(@Param("keyword") String keyword);

    @Query("SELECT p FROM TouristPlace p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<TouristPlace> searchAllPlaces(@Param("keyword") String keyword);
}