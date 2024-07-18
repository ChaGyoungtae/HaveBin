package com.HaveBinProject.HaveBin.RequestDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendReportTrashcanDTO {
    private Long id;
    private Long userId;
    private Long trashcanId;
    private String reportCategory;
    private int modifyStatus;

}

