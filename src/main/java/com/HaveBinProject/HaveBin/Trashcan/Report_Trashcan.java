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

    @Id @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="trashcan_id")
    private Trashcan trashcan;

    @Column(nullable = false)
    private String report_category;


}
