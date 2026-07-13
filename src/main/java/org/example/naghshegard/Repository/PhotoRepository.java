package org.example.naghshegard.Repository;

import org.example.naghshegard.Model.Photos;
import org.example.naghshegard.Model.TouristPlace;
import org.example.naghshegard.Model.Travelogue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photos, Long> {

    List<Photos> findByPlace(TouristPlace place);
    List<Photos> findByPlaceId(Long placeId);
    void deleteByPlace(TouristPlace place);
    List<Photos> findByTravelogue(Travelogue travelogue);
    List<Photos> findByTravelogueId(Long travelogueId);
    void deleteByTravelogue(Travelogue travelogue);
    long count();
}