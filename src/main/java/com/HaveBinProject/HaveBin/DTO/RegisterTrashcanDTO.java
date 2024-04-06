package com.HaveBinProject.HaveBin.DTO;

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
}
