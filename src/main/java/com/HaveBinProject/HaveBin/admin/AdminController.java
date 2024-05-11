package com.HaveBinProject.HaveBin.admin;

import com.HaveBinProject.HaveBin.RequestDTO.DeleteTrashcanDTO;
import com.HaveBinProject.HaveBin.RequestDTO.ReportDTO;
import com.HaveBinProject.HaveBin.RequestDTO.ReportTrashcanDTO;
import com.HaveBinProject.HaveBin.RequestDTO.SendReportTrashcanDTO;
import com.HaveBinProject.HaveBin.Trashcan.ShowReportTrashcan;
import com.HaveBinProject.HaveBin.Trashcan.Unknown_Trashcan;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/findAllUnknownTrashcans")
    public List<Unknown_Trashcan> adminPage(){
        return adminService.findAll();
    }

    // 임시 쓰레기통 -> 새로운 쓰레기통으로 등록
    @PostMapping("/acceptNewTrashcan")
    public ResponseEntity<?> acceptNewTrashcan(@RequestBody String unknown_trashcan_id) {
        System.out.println("unknown_trashcan_id = " + unknown_trashcan_id);
        return adminService.acceptNewTrashcan(Long.parseLong(unknown_trashcan_id));
    }

    // 새로운 쓰레기통 등록 후 임시 쓰레기통 데이터 삭제
    @PostMapping("/deleteUnknownTrashcan")
    public ResponseEntity<?> deleteUnknownTrashcan(@RequestBody Long unknown_trashcan_id) {
        System.out.println("unknown_trashcan_id = " + unknown_trashcan_id);
        return adminService.deleteUnknownTrashcan(unknown_trashcan_id);
    }

    //기존에 있는 쓰레기통 삭제
    // 삭제 하기 전에 다른 유저들의 해당 쓰레기통 신고 내역 삭제
    // 삭제 되면 ShowReportTrashcan에 modifyStatus에 반영
    @PostMapping("/deleteTrashcan")
    public ResponseEntity<?> deleteTrashcan(@RequestBody ReportTrashcanDTO reportTrashcanDTO) {


        Long trashcanId = Long.parseLong(reportTrashcanDTO.getTrashcanId());
        String category = reportTrashcanDTO.getReportCategory();

        try {
            adminService.deleteReportTrashcans(trashcanId, reportTrashcanDTO.getReportCategory());

            //사용자들 조회테이블에 해당 쓰레기통 신고내역에 modifyStatus를 1로 변경
            adminService.modifyStatus(reportTrashcanDTO);


        } catch (Exception e) {
            ResponseEntity.badRequest().body("신고내역 삭제 실패");
        }

        return adminService.deleteTrashcan(trashcanId);
    }

    //신고한 쓰레기통 목록 조회
    @GetMapping("/findAllReportTrashcan")
    public List<SendReportTrashcanDTO> findAllReportTrashcan(){
        return adminService.findAllReportTrashcan();
    }


    //reportTrashcan에 있는 쓰레기통 데이터 수정
    @PostMapping("/modifyTrashcan")
    public ResponseEntity<?> modifyTrashcan(@RequestBody ReportDTO reportDTO){
        return adminService.modifyTrashcan(reportDTO);
    }

}
