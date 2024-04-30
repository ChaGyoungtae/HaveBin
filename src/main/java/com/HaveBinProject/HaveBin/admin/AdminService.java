package com.HaveBinProject.HaveBin.admin;

import com.HaveBinProject.HaveBin.AWS.ImageService;
import com.HaveBinProject.HaveBin.RequestDTO.SendReportTrashcanDTO;
import com.HaveBinProject.HaveBin.Trashcan.Report_Trashcan;
import com.HaveBinProject.HaveBin.Trashcan.Trashcan;
import com.HaveBinProject.HaveBin.Trashcan.TrashcanRepository;
import com.HaveBinProject.HaveBin.Trashcan.Unknown_Trashcan;
import jakarta.persistence.Tuple;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final ImageService imageService;
    public List<Unknown_Trashcan> findAll(){ return adminRepository.findAllUnknownTrashcan(); }

    public ResponseEntity<?> acceptNewTrashcan(Long unknown_trashcan_id){
        Trashcan trashcan = new Trashcan();
        Unknown_Trashcan findUnknownTrashcan  = null;
        try {
            findUnknownTrashcan = adminRepository.findUnknownTrashcan(unknown_trashcan_id);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("등록 실패");
        }

        trashcan.setLatitude(findUnknownTrashcan.getLatitude());
        trashcan.setLongitude(findUnknownTrashcan.getLongitude());
        trashcan.setRoadviewImgpath(imageService.moveFileInS3(findUnknownTrashcan.getRoadviewImgpath()));
        trashcan.setUserId(findUnknownTrashcan.getUserId());
        trashcan.setCategories(findUnknownTrashcan.getCategories());
        trashcan.setState(findUnknownTrashcan.getState());
        trashcan.setDate(findUnknownTrashcan.getDate());

        return ResponseEntity.ok(adminRepository.acceptNewTrashcan(trashcan));
    }

    public ResponseEntity<?> deleteUnknownTrashcan(Long unknownTrashcanId){

        try{
            adminRepository.delete_UnknownTrashcan(unknownTrashcanId);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("삭제가 완료되지 않았습니다.");
        }

        return ResponseEntity.ok("삭제완료");
    }


    public ResponseEntity<?> deleteTrashcan(Long trashcanId){
        try {
            adminRepository.deleteTrashcan(trashcanId);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("삭제 실패");
        }
        return ResponseEntity.ok("삭제완료");
    }

    public List<SendReportTrashcanDTO> findAllReportTrashcan(){
        return adminRepository.findAllReportTrashcan();
    }



}
