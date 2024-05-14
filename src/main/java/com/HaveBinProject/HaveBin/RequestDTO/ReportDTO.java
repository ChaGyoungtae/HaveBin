package com.HaveBinProject.HaveBin.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private String reportId;
    private String trashcanId;
    private Double latitude;
    private Double longitude;
    private String reportCategory;
}
