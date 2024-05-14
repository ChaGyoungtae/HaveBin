package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class Report_Trashcan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="trashcan_id", nullable = false)
    private Trashcan trashcan;

    @Column(nullable = false)
    private String reportCategory;

    //false 면 처리중(빨간색글씨) / ture 면 처리완료(초록색글씨)
    @Column(nullable = false)
    private int modifyStatus;

    public Report_Trashcan(User user, Trashcan trashcan, String reportCategory) {
        this.user = user;
        this.trashcan = trashcan;
        this.reportCategory = reportCategory;
        modifyStatus = 0;
    }

}
