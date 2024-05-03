package com.HaveBinProject.HaveBin.ResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TrashcanData {

    private Long id;

    private double latitude;

    private double longitude;

    private String roadviewImgpath;

    private String nickname;

    private String categories;

    private String state;

    // 발견 날짜 (YOLO는 0301)
    private String date;

    // 주소 (주소는 없어도 가능)
    private String address;

    // 세부주소 (예, 버스정거장, 택시승강장, 공원 등)
    private String detailAddress;

    // 생성자
    public TrashcanData(Long id, String address, String categories, String date, String detailAddress,
                        double latitude, double longitude, String roadviewImgpath, String state, String nickname) {
        this.id = id;
        this.address = address;
        this.categories = categories;
        this.date = date;
        this.detailAddress = detailAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.roadviewImgpath = roadviewImgpath;
        this.state = state;
        this.nickname = nickname;
    }
}
