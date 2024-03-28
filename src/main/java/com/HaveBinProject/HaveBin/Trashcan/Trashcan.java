package com.HaveBinProject.HaveBin.Trashcan;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Trashcan {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private String roadviewImgpath;

    @Column(nullable = false)
    private Long userId;

    @Column
    private Category categories;

    @Column(nullable = false)
    private State state;


}
