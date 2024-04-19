package com.HaveBinProject.HaveBin.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    //화면 기준 왼쪽 밑 위경도 / 오른쪽 위 위경도
    private Double neLat;
    private Double neLon;

    private Double swLat;
    private Double swLon;
}
