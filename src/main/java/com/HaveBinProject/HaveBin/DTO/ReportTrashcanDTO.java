package com.HaveBinProject.HaveBin.DTO;

import com.HaveBinProject.HaveBin.Trashcan.Trashcan;
import com.HaveBinProject.HaveBin.User.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportTrashcanDTO {

    private String trashcanId;
    private String report_category;

}
