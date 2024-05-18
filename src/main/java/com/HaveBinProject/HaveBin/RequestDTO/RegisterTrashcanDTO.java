package com.HaveBinProject.HaveBin.RequestDTO;

import com.HaveBinProject.HaveBin.Trashcan.Unknown_Trashcan;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RegisterTrashcanDTO {

    // 정렬 메서드 추가
    public void sortCoordinates() {
        Collections.sort(coordinates, new Comparator<Coordinate>() {
            @Override
            public int compare(Coordinate c1, Coordinate c2) {
                int latitudeComparison = Double.compare(c1.getLatitude(), c2.getLatitude());
                if (latitudeComparison != 0) {
                    return latitudeComparison;
                }
                return Double.compare(c1.getLongitude(), c2.getLongitude());
            }
        });
    }

    public Coordinate getLatAndLon (List<Coordinate> coordinates) {

        double Lat = 0;
        double Lon = 0;

        // 위도 기준으로 좌표 정렬 -> 맨 앞과 맨 끝 값 삭제 -> 나머지 평균 구하기
        List<Coordinate> cordinates = this.getCoordinates();
        System.out.println("sort start!");
        for(int i = 0; i<cordinates.size(); i++){
            System.out.println(i + " : " + cordinates.get(i).getLatitude()+" , "+cordinates.get(i).getLongitude());
        }
        sortCoordinates();
        System.out.println("sort end!");
        System.out.println("median start!");
        if (coordinates.size() > 1) {
            coordinates.remove(0); // 맨 앞의 값 제거
            coordinates.remove(coordinates.size() - 1); // 맨 뒤의 값 제거
        }

        for (int i = 0; i < coordinates.size(); i++) {
            Lat += coordinates.get(i).getLatitude();
            Lon += coordinates.get(i).getLongitude();
        }
        
        return new Coordinate(Lat / coordinates.size(), Lon / coordinates.size());
    }

    private List<Coordinate> coordinates;
    private String categories;
    private String detailAddress;

    public Unknown_Trashcan toEntity(RegisterTrashcanDTO registerTrashcanDTO, Long userid) {
        Unknown_Trashcan trashcan = new Unknown_Trashcan();


        

        Coordinate coordinate = getLatAndLon(coordinates);
        System.out.println("coordinate.getLatitude() + \" , \" + coordinate.getLongitude() = " + coordinate.getLatitude() + " , " + coordinate.getLongitude());
        System.out.println("end!");

        trashcan.setUserId(userid);
        trashcan.setLatitude(coordinate.getLatitude());
        trashcan.setLongitude(coordinate.getLongitude());
        trashcan.setCategories(registerTrashcanDTO.getCategories());
        trashcan.setState("impossible");
        trashcan.setDetailAddress(registerTrashcanDTO.getDetailAddress());

        Date currentDate = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        String formattedDate = dateFormat.format(currentDate);

        trashcan.setDate(formattedDate);
        //trashcan.setRoadviewImgpath(imageService.uploadImageToS3(image, "Unknown_Trashcan"));
//        try {
//            trashcan.setRoadviewImgpath(imageService.uploadImageToS3(registerTrashcanDTO.getImage(), "Unknown_Trashcan"));
//        } catch (Exception e) {
//            System.out.println("이미지 업로드 실패");
//        }

        return trashcan;
    }
}
