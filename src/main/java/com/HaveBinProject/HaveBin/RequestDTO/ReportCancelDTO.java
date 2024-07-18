package com.HaveBinProject.HaveBin.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportCancelDTO {

    private String trashcanId;
    private String reportCategory;
    private String reasonReportCancel;
}
