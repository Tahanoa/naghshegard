package org.example.naghshegard.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "photos")
public class Photos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "bytea")
    private byte[] imageData;

    private String contentType;
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private TouristPlace place;

    @ManyToOne
    @JoinColumn(name = "travelogue_id")
    private Travelogue travelogue;

    public Photos() {
    }

    public Photos(byte[] imageData, String contentType, String fileName) {
        this.imageData = imageData;
        this.contentType = contentType;
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public TouristPlace getPlace() {
        return place;
    }

    public void setPlace(TouristPlace place) {
        this.place = place;
    }

    public Travelogue getTravelogue() {
        return travelogue;
    }

    public void setTravelogue(Travelogue travelogue) {
        this.travelogue = travelogue;
    }
}