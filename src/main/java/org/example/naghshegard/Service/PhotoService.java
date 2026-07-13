package org.example.naghshegard.Service;

import org.example.naghshegard.Model.Photos;
import org.example.naghshegard.Repository.PhotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Photos getPhotoById(Long id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Photo not found"));
    }

    public List<Photos> getPhotosByPlaceId(Long placeId) {
        return photoRepository.findByPlaceId(placeId);
    }
}