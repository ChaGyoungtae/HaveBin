package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.RequestDTO.*;
import com.HaveBinProject.HaveBin.RequestDTO.PosResponse;
import com.HaveBinProject.HaveBin.ResponseDTO.TrashcanData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://172.30.1.27:3000", "http://localhost:3000"}, allowCredentials = "true")
public class TrashcanController {

    private final TrashcanService trashcanService;

    @GetMapping("/findTrashcans")
    public List<TrashcanData> sendAll(){
        return trashcanService.findTrashcans();
    }

    @PostMapping("/getGPS")
    public List<TrashcanData> sendTrashcan(@RequestBody PosResponse dto){
        return trashcanService.findNearTrashcans(dto);
    }

    //유저가 새로 신고한 쓰레기통 데이터를 일단 unknown_trashcan 테이블에 저장
    @PostMapping("/newTrashcan")
    public ResponseEntity<?> newTrashcan(@RequestPart(value = "RegisterTrashcanDTO") RegisterTrashcanDTO registerTrashcanDTO, @RequestPart(value = "image") MultipartFile files , @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        return trashcanService.register_unknown(registerTrashcanDTO, files, email);
    }

    //유저가 기존에 있던 쓰레기통을 신고 / reportTrashcan에 저장
    @PostMapping("/reportTrashcan")
    public ResponseEntity<?> reportTrashcan(@RequestBody ReportTrashcanDTO reportTrashcanDTO, @AuthenticationPrincipal CustomUserDetails userDetails){

        String email = userDetails.getUsername();
        System.out.println("email = " + email);
        return trashcanService.reportTrashcan(reportTrashcanDTO, email);
    }

    //해당 쓰레기통을 신고한 사람의 수 조회(신고 횟수)
    @GetMapping("/findReportCount")
    public int findReportCount(@RequestBody ReportCountDTO reportCountDTO){
        Long TrashcanId = Long.parseLong(reportCountDTO.getTrashcanId());
        return trashcanService.findReportCount(TrashcanId);
    }

    //유저가 신고한 쓰레기통 삭제
    @PostMapping("/deleteReportTrashcan")
    public ResponseEntity<?> deleteReportTrashcan(@RequestBody ReportTrashcanDTO deleteReportTrashcanDTO){
        return trashcanService.deleteReportTrashcan(deleteReportTrashcanDTO);
    }

    @PostMapping("/findReportTrashcans")
    public List<SendReportTrashcanDTO> findReportTrashcans(@AuthenticationPrincipal CustomUserDetails userDetails){
        String email = userDetails.getUsername();
        return trashcanService.findReportTrashcans(email);
    }
}
