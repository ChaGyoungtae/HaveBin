package com.HaveBinProject.HaveBin.Trashcan;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Trashcan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    // 발견 날짜 (YOLO는 0301)
    @Column(nullable = false)
    private String date;

    // 주소 (주소는 없어도 가능)
    @Column
    private String address;

    // 세부주소 (예, 버스정거장, 택시승강장, 공원 등)
    @Column
    private String detailAddress;
}
