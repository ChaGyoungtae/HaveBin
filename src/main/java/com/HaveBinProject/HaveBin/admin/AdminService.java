package com.HaveBinProject.HaveBin.admin;

import com.HaveBinProject.HaveBin.AWS.ImageService;
import com.HaveBinProject.HaveBin.RequestDTO.ReportDTO;
import com.HaveBinProject.HaveBin.RequestDTO.ReportTrashcanDTO;
import com.HaveBinProject.HaveBin.RequestDTO.SendReportTrashcanDTO;
import com.HaveBinProject.HaveBin.Trashcan.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

        double lat = findUnknownTrashcan.getLatitude();
        double lon = findUnknownTrashcan.getLongitude();

        trashcan.setLatitude(lat);
        trashcan.setLongitude(lon);
        trashcan.setRoadviewImgpath(imageService.moveFileInS3(findUnknownTrashcan.getRoadviewImgpath()));
        trashcan.setUserId(findUnknownTrashcan.getUserId());
        trashcan.setCategories(findUnknownTrashcan.getCategories());
        trashcan.setState("possible");
        trashcan.setDate(findUnknownTrashcan.getDate());
        trashcan.setDetailAddress(findUnknownTrashcan.getDetailAddress());

        Reverse_Geocoding reverseGeocoding = new Reverse_Geocoding();

        String address = null;
        try {
            address = reverseGeocoding.loadLocation(lat,lon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.out.println("address = " + address);
        trashcan.setAddress(address);
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
            //삭제하기전 신고한 유저들 해당 쓰레기통 신고내역 삭제
            adminRepository.deleteTrashcan(trashcanId);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("삭제 실패");
        }
        return ResponseEntity.ok("삭제완료");
    }

    public List<SendReportTrashcanDTO> findAllReportTrashcan(){
        return adminRepository.findAllReportTrashcan();
    }


    public ResponseEntity<?> modifyTrashcan(ReportDTO reportDTO){

        try{
            adminRepository.modifyTrashcan(reportDTO);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("수정 실패");
        }
        return ResponseEntity.ok("수정 완료");
    }

    public ResponseEntity<?> deleteReportTrashcans(Long reportTrashcanId, String reportCategory){
        try{
            adminRepository.deleteReportTrashcans(reportTrashcanId,reportCategory);
        } catch (Exception e){
            ResponseEntity.badRequest().body("신고 내역 삭제 실패");
        }
        return ResponseEntity.ok("신고 내역 삭제 완료");
    }

    public ResponseEntity<?> modifyStatus(ReportTrashcanDTO reportTrashcanDTO){
        Long trashcanId = Long.parseLong(reportTrashcanDTO.getTrashcanId());
        String reportCategory = reportTrashcanDTO.getReportCategory();
        try{
            List<ShowReportTrashcan> showReportTrashcanList = adminRepository.findShowReportTrashcanByTrashcanIdandReportCategory(trashcanId,reportCategory);
            //status 모두 1로 수정
            for (ShowReportTrashcan trashcan: showReportTrashcanList){
                adminRepository.modifyStatus(trashcan);
            }
        } catch (IllegalStateException e){
            ResponseEntity.badRequest().body("modifyStatus 실패");
        }

        return ResponseEntity.ok("modifyStatus 성공");

    }
}
