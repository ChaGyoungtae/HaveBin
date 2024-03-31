package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.DTO.ResponseDTO;
import lombok.RequiredArgsConstructor;
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
}
