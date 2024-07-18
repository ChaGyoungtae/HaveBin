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
    private Long unknown_trashcan_id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private String roadviewImgpath;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String categories;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String detailAddress;
}
