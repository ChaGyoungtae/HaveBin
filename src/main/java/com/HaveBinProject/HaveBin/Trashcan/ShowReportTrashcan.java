package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class ShowReportTrashcan {


    //조회용 테이블
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private Long trashcanId;

    @Column
    private String detailAddress;

    @Column(nullable = false)
    private String reportCategory;

    //false 면 처리중(빨간색글씨) / ture 면 처리완료(초록색글씨)
    @Column(nullable = false)
    private Boolean modifyStatus;

    public ShowReportTrashcan(String email, Long trashcanId, String detailAddress, String reportCategory) {
        this.email = email;
        this.trashcanId = trashcanId;
        this.reportCategory = reportCategory;
        this.detailAddress = detailAddress;
        modifyStatus = false;
    }
}
