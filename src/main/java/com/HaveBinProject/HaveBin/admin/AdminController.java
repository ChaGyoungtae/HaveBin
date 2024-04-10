package com.HaveBinProject.HaveBin.admin;

import com.HaveBinProject.HaveBin.Trashcan.Unknown_Trashcan;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
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

    @GetMapping("/adminPage")
    public List<Unknown_Trashcan> adminPage(){
        return adminService.findAll();
    }

    // 임시 쓰레기통 -> 새로운 쓰레기통으로 등록
    @PostMapping("/acceptNewTrashcan")
    public ResponseEntity<?> acceptNewTrashcan(@RequestBody String unknown_trashcan_id) {
        return adminService.acceptNewTrashcan(Long.parseLong(unknown_trashcan_id));
    }

    // 새로운 쓰레기통 등록 후 임시 쓰레기통 데이터 삭제
    @PostMapping("/deleteUnknownTrashcan")
    public  ResponseEntity<?> deleteUnknownTrashcan(@RequestBody String unknown_trashcan_id) {
        return adminService.deleteUnknownTrashcan(Long.parseLong(unknown_trashcan_id));
    }
}