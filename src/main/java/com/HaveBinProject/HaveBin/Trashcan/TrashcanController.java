package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.DTO.RegisterTrashcanDTO;
import com.HaveBinProject.HaveBin.DTO.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TrashcanController {

    private final TrashcanService trashcanService;

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
    public ResponseEntity<?> newTrashcan(@RequestBody RegisterTrashcanDTO registerTrashcanDTO){
        return trashcanService.register_unknown(registerTrashcanDTO);
    }



}
