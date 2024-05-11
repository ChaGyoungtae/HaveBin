package com.HaveBinProject.HaveBin.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyReportTrashcanDTO {

    private Long id;
    private String email;
    private Long trashcanId;
    private String reportCategory;
    private Boolean modifyStatus;
    private String detailAddress;
}
