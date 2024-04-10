package com.HaveBinProject.HaveBin.DTO;

import com.HaveBinProject.HaveBin.Trashcan.Trashcan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterTrashcanDTO {

    private double latitude;
    private double longitude;
    private String roadviewImgpath;
    private Long userId;
    private String categories;
    private String state;
    private String report_date;
}
