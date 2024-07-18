package com.HaveBinProject.HaveBin.admin;

import com.HaveBinProject.HaveBin.AWS.ImageService;
import com.HaveBinProject.HaveBin.RequestDTO.ReportDTO;
import com.HaveBinProject.HaveBin.RequestDTO.ReportTrashcanDTO;
import com.HaveBinProject.HaveBin.RequestDTO.SendReportTrashcanDTO;
import com.HaveBinProject.HaveBin.Trashcan.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final TrashcanRepository trashcanRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ImageService imageService;
    public List<Unknown_Trashcan> findAll(){ return adminRepository.findAllUnknownTrashcan(); }

    public ResponseEntity<String> acceptNewTrashcan(Long unknown_trashcan_id){
        Trashcan trashcan = new Trashcan();
        Unknown_Trashcan findUnknownTrashcan  = null;
        try {
            findUnknownTrashcan = adminRepository.findUnknownTrashcan(unknown_trashcan_id);
        } catch (Exception e) {
            logger.error("acceptNewTrashcan - 새 쓰레기통 승인 실패(승인하려는 Unknown Trashcan이 Trashcan 테이블에 있는 경우 오류)");
            return ResponseEntity.badRequest().body("8");
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
            logger.error("acceptNewTrashcan - 역 지오코딩 실패");
            return ResponseEntity.badRequest().body("9");
        }


        System.out.println("address = " + address);
        trashcan.setAddress(address);
        adminRepository.acceptNewTrashcan(trashcan);
        logger.info("acceptNewTrashcan 성공");
        return ResponseEntity.ok("10");
    }

    public ResponseEntity<String> deleteUnknownTrashcan(Long unknownTrashcanId){

        try{
            adminRepository.delete_UnknownTrashcan(unknownTrashcanId);
        } catch (Exception e) {
            logger.error("Unknown Trashcan - 해당 데이터 삭제 오류");
            ResponseEntity.badRequest().body("11");
        }
        logger.info("Unknown Trashcan에 해당 데이터 삭제 성공");
        return ResponseEntity.ok("12");
    }


    public ResponseEntity<?> deleteTrashcan(Long trashcanId){
        try {
            //삭제하기전 신고한 유저들 해당 쓰레기통 신고내역 삭제
            adminRepository.deleteTrashcan(trashcanId);
        } catch (Exception e) {
            logger.error("deleteTrashcan - Trashcan 삭제 실패");
            return ResponseEntity.badRequest().body("15");
        }
        logger.info("deleteTrashcan - 삭제 성공");
        return ResponseEntity.ok("16");
    }

    public List<SendReportTrashcanDTO> findAllReportTrashcan(){
        return adminRepository.findAllReportTrashcan();
    }


    public ResponseEntity<?> modifyTrashcan(ReportDTO reportDTO){

        Long trashcanId = Long.parseLong(reportDTO.getTrashcanId());
        String category = reportDTO.getReportCategory();

        try {
            deleteReportTrashcans(trashcanId, category);
        } catch (Exception e) {
            logger.error("modifyTrashcan - 해당 Trashcan을 같은 신고항목으로 신고한 다른 신고내역들 삭제 실패");
            return ResponseEntity.badRequest().body("17");
        }

        try {
            modifyStatus(trashcanId,category, 1);
        } catch (Exception e) {
            logger.error("modifyTrashcan - 신고를 처리한 신고내역에 대해 조회용 신고내역의 modifyStatus를 1로 변경 실패");
            return ResponseEntity.badRequest().body("18");
        }
        Trashcan trashcan = new Trashcan();
        try{
            Reverse_Geocoding reverseGeocoding = new Reverse_Geocoding();
            String addresss = reverseGeocoding.loadLocation(reportDTO.getLatitude(), reportDTO.getLongitude());
            trashcan = trashcanRepository.find(trashcanId);
            trashcan.setLongitude(reportDTO.getLongitude());
            trashcan.setLatitude(reportDTO.getLatitude());
            trashcan.setDetailAddress(addresss);
        } catch (Exception e){
            logger.error("modifyTrashcan - 바꾼 위경도에 대한 지오코딩 실패");
            return ResponseEntity.badRequest().body("19");
        }
        adminRepository.modifyTrashcan(trashcan);
        logger.info("modifyTrashcan - 쓰레기통 정보 수정 성공");
        return ResponseEntity.ok("20");
    }

    public ResponseEntity<?> deleteReportTrashcans(Long reportTrashcanId, String reportCategory){
        try{
            adminRepository.deleteReportTrashcans(reportTrashcanId,reportCategory);
        } catch (Exception e){
           return ResponseEntity.badRequest().body("deleteReportTrashcans 실패");
        }
        return ResponseEntity.ok("deleteReportTrashcans 성공");
    }

    public ResponseEntity<?> modifyStatus(Long reportTrashcanId, String reportCategory,int i){
        try{
            List<ShowReportTrashcan> showReportTrashcanList = adminRepository.findShowReportTrashcanByTrashcanIdandReportCategory(reportTrashcanId,reportCategory);
            //status 모두 1로 수정
            for (ShowReportTrashcan trashcan: showReportTrashcanList){
                trashcan.setModifyStatus(i);
                adminRepository.modifyStatus(trashcan);
            }
        } catch (IllegalStateException e){
            return ResponseEntity.badRequest().body("14");
        }
        logger.info("modifyStatus 성공");
        return ResponseEntity.ok("modifyStatus 성공");

    }

    public ResponseEntity<?> cancelReport(Long reportTrashcanId, String reportCategory, String reason){
        try {
            deleteReportTrashcans(reportTrashcanId, reportCategory);
        } catch (Exception e) {
            logger.error("cancelTrashcan - 해당 Trashcan을 같은 신고항목으로 신고한 다른 신고내역들 삭제 실패");
            return ResponseEntity.badRequest().body("21");
        }

        try {
            //사용자들 조회테이블에 해당 쓰레기통 신고내역에 modifyStatus를 2로 변경
            modifyStatus(reportTrashcanId,reportCategory,2);
        } catch (Exception e){
            logger.error("cancelTrashcan - 신고를 처리한 신고내역에 대해 조회용 신고내역의 modifyStatus를 2로 변경 실패");
            return ResponseEntity.badRequest().body("22");
        }

        try {
            //신고 취소 사유 저장
            List<ShowReportTrashcan> showReportTrashcanList = adminRepository.findShowReportTrashcanByTrashcanIdandReportCategory(reportTrashcanId,reportCategory);
            //status 모두 1로 수정
            for (ShowReportTrashcan trashcan: showReportTrashcanList){
                trashcan.setReasonReportCancel(reason);
                adminRepository.modifyStatus(trashcan);
            }
        } catch (Exception e){
            logger.error("cancelTrashcan - 신고사유수정 실패");
            return ResponseEntity.badRequest().body("24");
        }
            logger.info("cancelTrashcan 성공");
        return ResponseEntity.ok().body("23");
    }
}
