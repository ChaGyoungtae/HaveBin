package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrashcanController {

    private final TrashcanService trashcanService;

    //String role = userDetails.getAuthorities().toString();

    @GetMapping("/findTrashcans")
    public List<Trashcan> sendAll(){
        return trashcanService.findTrashcans();
    }

    @PostMapping("/getGPS")
    public List<Trashcan> sendTrashcan(@RequestBody ResponseDTO responseDTO){
        System.out.println(responseDTO.toString());
        return trashcanService.findNearTrashcans(responseDTO);
    }

    //유저가 새로 신고한 쓰레기통 데이터를 일단 unknown_trashcan 테이블에 저장
    @PostMapping("/newTrashcan")
    public ResponseEntity<?> newTrashcan(@RequestBody RegisterTrashcanDTO registerTrashcanDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        return trashcanService.register_unknown(registerTrashcanDTO, email);
    }

    //유저가 기존에 있던 쓰레기통을 신고 / reportTrashcan에 저장
    @PostMapping("/reportTrashcan")
    public ResponseEntity<?> reportTrashcan(@RequestBody ReportTrashcanDTO reportTrashcanDTO, @AuthenticationPrincipal CustomUserDetails userDetails){

        String email = userDetails.getUsername();
        return trashcanService.reportTrashcan(reportTrashcanDTO, email);
    }

    //해당 쓰레기통을 신고한 사람의 수 조회(신고 횟수)
    @GetMapping("/findReportCount")
    public int findReportCount(@RequestBody ReportCountDTO reportCountDTO){
        Long TrashcanId = Long.parseLong(reportCountDTO.getTrashcanId());
        return trashcanService.findReportCount(TrashcanId);
    }
}
