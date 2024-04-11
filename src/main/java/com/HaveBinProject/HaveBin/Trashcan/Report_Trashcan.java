package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
public class Report_Trashcan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="trashcan_id")
    private Trashcan trashcan;

    @Column(nullable = false)
    private String report_category;

    //false 면 처리중(빨간색글씨) / ture 면 처리완료(초록색글씨)
    @Column(nullable = false)
    private Boolean ModifyStatus;
}
