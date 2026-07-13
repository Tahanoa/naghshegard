package org.example.naghshegard.Repository;

import org.example.naghshegard.Model.Travelogue;
import org.example.naghshegard.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TravelogueRepository extends JpaRepository<Travelogue, Long> {

    List<Travelogue> findByUserAndApproved(User user, boolean approved);

    List<Travelogue> findByUser(User user);

    Page<Travelogue> findByApprovedTrue(Pageable pageable);

    List<Travelogue> findByApprovedFalse();

    @Query("SELECT t FROM Travelogue t WHERE t.approved = true AND (t.title LIKE %:keyword% OR t.content LIKE %:keyword%)")
    Page<Travelogue> searchApprovedTravelogues(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT t FROM Travelogue t WHERE t.approved = true ORDER BY t.viewCount DESC")
    List<Travelogue> findMostViewedTravelogues(Pageable pageable);

    @Query("SELECT t FROM Travelogue t WHERE t.approved = true ORDER BY t.likeCount DESC")
    List<Travelogue> findMostLikedTravelogues(Pageable pageable);

    @Query("SELECT t FROM Travelogue t WHERE t.approved = true ORDER BY t.createdAt DESC")
    List<Travelogue> findLatestTravelogues(Pageable pageable);

    @Query("SELECT t FROM Travelogue t WHERE t.title LIKE %:keyword% OR t.content LIKE %:keyword%")
    List<Travelogue> searchAllTravelogues(@Param("keyword") String keyword);

    long countByApprovedFalse();
    long countByApprovedTrue();
}