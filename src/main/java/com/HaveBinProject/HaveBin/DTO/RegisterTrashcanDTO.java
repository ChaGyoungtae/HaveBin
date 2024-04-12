package com.HaveBinProject.HaveBin.DTO;

import com.HaveBinProject.HaveBin.AWS.ImageService;
import com.HaveBinProject.HaveBin.Trashcan.Trashcan;
import com.HaveBinProject.HaveBin.Trashcan.Unknown_Trashcan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class RegisterTrashcanDTO {

    private final ImageService imageService;

    private double latitude;
    private double longitude;
    private String roadviewImgpath;
    private String categories;
    private String state;
    private String report_date;
    private MultipartFile image;

    public Unknown_Trashcan toEntity(RegisterTrashcanDTO registerTrashcanDTO, Long userid) {
        Unknown_Trashcan trashcan = new Unknown_Trashcan();

        trashcan.setUserId(userid);
        trashcan.setLatitude(registerTrashcanDTO.getLatitude());
        trashcan.setLongitude(registerTrashcanDTO.getLongitude());
        trashcan.setRoadviewImgpath(registerTrashcanDTO.getRoadviewImgpath());
        trashcan.setCategories(registerTrashcanDTO.getCategories());
        trashcan.setState(registerTrashcanDTO.getState());
        trashcan.setDate(registerTrashcanDTO.getReport_date());
        trashcan.setRoadviewImgpath(imageService.uploadImageToS3(registerTrashcanDTO.getImage(), "Unknown_Trashcan"));

        return trashcan;
    }
}
