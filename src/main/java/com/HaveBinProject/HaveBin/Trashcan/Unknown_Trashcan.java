package com.HaveBinProject.HaveBin.Trashcan;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Unknown_Trashcan {

    @Id @GeneratedValue
    private Long trashcan_id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private String roadviewImgpath;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Category categories;

}