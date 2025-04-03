package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.RequestDTO.ReportCountDTO;
import com.HaveBinProject.HaveBin.ResponseDTO.TrashcanData;
import com.HaveBinProject.HaveBin.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TrashcanControllerV2 {

    private final TrashcanService trashcanService;

    //해당 쓰레기통을 신고한 사람의 수 조회(신고 횟수)
    @PostMapping("/findReportCount")
    public int findReportCount(@RequestBody ReportCountDTO reportCountDTO){
        System.out.println("reportCountDTO.getTrashcanId() = " + reportCountDTO.getTrashcanId());
        return trashcanService.findReportCount(reportCountDTO.getTrashcanId());
    }

    @GetMapping("/findTrashcans")
    public List<TrashcanData> sendAll(){
        return trashcanService.findTrashcans();
    }
}
