package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.RequestDTO.*;
import com.HaveBinProject.HaveBin.ResponseDTO.TrashcanData;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {("http://localhost:3000"),("http://172.30.1.9:3000")})
public class TrashcanController {

    private final TrashcanService trashcanService;
    private final ReportIntervalService reportIntervalService;

    @GetMapping("/findTrashcans")
    public List<TrashcanData> sendAll(){
        return trashcanService.findTrashcans();
    }

    @PostMapping("/getGPS")
    public List<TrashcanData> sendTrashcan(@RequestBody PosResponse posResponse){
        System.out.println(posResponse.toString());
        return trashcanService.findNearTrashcans(posResponse);
    }

    //유저가 새로 신고한 쓰레기통 데이터를 일단 unknown_trashcan 테이블에 저장
    @PostMapping("/newTrashcan")
    public ResponseEntity<?> newTrashcan(@RequestPart(value = "RegisterTrashcanDTO") RegisterTrashcanDTO registerTrashcanDTO, @RequestPart(value = "image") MultipartFile files , @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        return trashcanService.register_unknown(registerTrashcanDTO, files, email);
    }

    //유저가 기존에 있던 쓰레기통을 신고 / reportTrashcan에 저장 / 조회용 테이블에도 저장
    @PostMapping("/reportTrashcan")
    public ResponseEntity<?> reportTrashcan(@RequestBody ReportTrashcanDTO reportTrashcanDTO, @AuthenticationPrincipal CustomUserDetails userDetails){

        String email = userDetails.getUsername();

        //관리자가 아니면서 1분 내로 이미 신고를 한 경우 신고 불가
        if(reportIntervalService.getData(email) != null) {
            if(userDetails.getUsername() != "admin")
                return ResponseEntity.badRequest().body("신고는 1분에 1번만 가능합니다. 잠시 뒤에 신고해주세요.");
        }

        System.out.println("email = " + email);
        System.out.println("id: "+reportTrashcanDTO.getTrashcanId());
        System.out.println("category: "+reportTrashcanDTO.getReportCategory());

        reportIntervalService.reportWithExpiration(email, email, 60);

        return trashcanService.reportTrashcan(reportTrashcanDTO, email);
    }

    //해당 쓰레기통을 신고한 사람의 수 조회(신고 횟수)
    @PostMapping("/findReportCount")
    public int findReportCount(@RequestBody ReportCountDTO reportCountDTO){
        return trashcanService.findReportCount(reportCountDTO.getTrashcanId());
    }

    //유저가 신고한 쓰레기통 삭제(조회테이블에서도 삭제)
    @PostMapping("/deleteReportTrashcan")
    public ResponseEntity<?> deleteReportTrashcan(@RequestBody ReportTrashcanDTO deleteReportTrashcanDTO, @AuthenticationPrincipal CustomUserDetails userDetails){
        String email = userDetails.getUsername();
        return trashcanService.deleteReportTrashcan(deleteReportTrashcanDTO, email);
    }


    @PostMapping("/myReportTrashcans")
    public List<MyReportTrashcanDTO> myReportTrashcans(@AuthenticationPrincipal CustomUserDetails userDetails){
        String email = userDetails.getUsername();
        return trashcanService.myReportTrashcans(email);
    }
}
