package org.example.naghshegard.Repository;

import org.example.naghshegard.Model.Review;
import org.example.naghshegard.Model.TouristPlace;
import org.example.naghshegard.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPlace(TouristPlace place);

    List<Review> findByPlaceIdAndApproved(Long placeId, boolean approved);

    List<Review> findByPlaceAndApproved(TouristPlace place, boolean approved);

    List<Review> findByUser(User user);

    List<Review> findByApproved(boolean approved);

    boolean existsByUserAndPlace(User user, TouristPlace place);

    List<Review> findByPlaceId(Long placeId);

    List<Review> findByApprovedFalse();

    List<Review> findAll();

    long countByApprovedFalse();

    @Query("SELECT r FROM Review r WHERE r.comment LIKE %:keyword% OR r.user.username LIKE %:keyword% OR r.place.name LIKE %:keyword%")
    List<Review> searchReviews(@Param("keyword") String keyword);

    void deleteByPlace(TouristPlace place);
}