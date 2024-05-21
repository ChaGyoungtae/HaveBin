package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.RequestDTO.*;
import com.HaveBinProject.HaveBin.AWS.ImageService;
import com.HaveBinProject.HaveBin.RequestDTO.RegisterTrashcanDTO;
import com.HaveBinProject.HaveBin.RequestDTO.ReportTrashcanDTO;
import com.HaveBinProject.HaveBin.RequestDTO.PosResponse;
import com.HaveBinProject.HaveBin.ResponseDTO.TrashcanData;
import com.HaveBinProject.HaveBin.User.User;
import com.HaveBinProject.HaveBin.User.UserRepository;
import com.HaveBinProject.HaveBin.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrashcanService {

    private final TrashcanRepository trashcanRepository;
    private final AdminService adminService;
    private final UserRepository userRepository;
    private final ImageService imageService;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    //새로운 쓰레기통 등록 아직 미구현
    // 5미터 반경에 쓰레기통이 이미 있으면 등록 취소
    public ResponseEntity<String> register_unknown(RegisterTrashcanDTO registerTrashcanDTO, MultipartFile file, String email){

        //Trashcan, UnknownTrashcan 두 테이블 모두 없는 신규 쓰레기통이면 등록
        try{
            registerTrashcanDTO.sortCoordinates();
            Coordinate coordinate = registerTrashcanDTO.getLatAndLon(registerTrashcanDTO.getCoordinates());

            Double lat = coordinate.getLatitude();
            Double lon = coordinate.getLongitude();

            //5미터는 0.0000449 - 직경 20미터내에 있는 쓰레기통 찾기
            Double interval = 0.0001796;
            PosResponse posResponse = new PosResponse(lat+interval,lon+interval, lat-interval, lon-interval );
            List<TrashcanData> trashcanDataList = findNearTrashcans(posResponse);
            if (trashcanDataList == null) {
                trashcanDataList = new ArrayList<>();
            }
            System.out.println("trashcanDataList = " + trashcanDataList);

            //등록되어있는 쓰레기통인지 판별
            if(trashcanDataList.size() > 0){
                throw new IllegalStateException("이미 등록된 쓰레기통 입니다.");
            }
            //이미 신고되어있는 쓰레기통인지 판별
            List<Unknown_Trashcan> unknownTrashcanList = trashcanRepository.ValidateDuplicateUnknownTrashcan(posResponse);
            if(unknownTrashcanList == null){
                unknownTrashcanList = new ArrayList<>();
            }
            System.out.println("unknownTrashcanList = " + unknownTrashcanList);

            if(unknownTrashcanList.size() > 0){
                throw new IllegalStateException("이미 등록된 쓰레기통 입니다.");
            }

            Long userId = userRepository.findIdByEmail(email);
            Unknown_Trashcan unknown_trashcan = registerTrashcanDTO.toEntity(registerTrashcanDTO, userId);

            unknown_trashcan.setRoadviewImgpath(imageService.uploadImageToS3(file,"Unknown_Trashcan"));

            Long trashcanId = trashcanRepository.saveOne_unknown(unknown_trashcan);
        } catch (IllegalStateException e){
            logger.error("Unknown Trashcan 등록 실패");

            return ResponseEntity.badRequest().body("1");
        }



        logger.info("Unknown Trashcan 등록 성공");
        return ResponseEntity.ok("2");
    }

    public Long findTrashcanIdByLatAndLon(Double lat, Double lon){
        return trashcanRepository.findTrashcanByLatAndLon(lat,lon);
    }

    //전체 쓰레기통 조회
    public List<TrashcanData> findTrashcans(){
        return trashcanRepository.findTrashcanData();
    }
    //특정 쓰레기통 조회
    public Trashcan findOnd(Long id){
        return trashcanRepository.find(id);
    }

    public List<TrashcanData> findNearTrashcans(PosResponse posResponse){
        List<TrashcanData> trashcanDatas= trashcanRepository.findNearTrashcanData(posResponse);

        if(trashcanDatas.isEmpty()) return null;
        else return trashcanDatas;
    }

    //쓰레기통 신고
    @Transactional
    public ResponseEntity<?> reportTrashcan(ReportTrashcanDTO reportTrashcanDTO, String email){

        try{
            String reportCategory = reportTrashcanDTO.getReportCategory();
            Long reportTrashcanId = Long.parseLong(reportTrashcanDTO.getTrashcanId());

            List<Report_Trashcan> findReportTrashcan = trashcanRepository.findReportTrashcanByIdAndReportCategoryAndTrashcanId(reportCategory, email, reportTrashcanId);

            if (findReportTrashcan.isEmpty() == false) {
                throw new IllegalStateException("이미 신고한 쓰레기통입니다.");
            }

            User user = userRepository.findByEmail(email).get(0);
            Long trashcanId = Long.parseLong(reportTrashcanDTO.getTrashcanId());
            Trashcan trashcan = trashcanRepository.find(trashcanId);
            //카테고리 / 0 : 부정확한 위치, 1:쓰레기통 없음
            Report_Trashcan reportTrashcan = new Report_Trashcan(user,trashcan,reportTrashcanDTO.getReportCategory());
            trashcanRepository.saveReportTrashcan(reportTrashcan);

            String address = trashcan.getDetailAddress();

            //조회용 테이블에도 저장
            ShowReportTrashcan showReportTrashcan = new ShowReportTrashcan(email,trashcanId,address,reportCategory);
            trashcanRepository.saveShowReportTrashcan(showReportTrashcan);

        } catch (IllegalStateException e) {
            logger.error("reportTrashcan - 중복 신고 에러");
            return ResponseEntity.badRequest().body("4");
        }
        logger.info("reportTrashcan - 신고 성공");
        return ResponseEntity.ok("5");

    }

    public int findReportCount(Long TrashcanId){
        return trashcanRepository.findReportCount(TrashcanId);
    }

    @Transactional
    public ResponseEntity<?> deleteReportTrashcan(ReportTrashcanDTO deleteReportTrashcanDTO, String email){
        try{
            Long reportTrashcanId = Long.parseLong(deleteReportTrashcanDTO.getTrashcanId());
            String reportCategory = deleteReportTrashcanDTO.getReportCategory();

            System.out.println(" ========================");
            System.out.println("reportCategory = " + reportCategory);
            System.out.println("reportTrashcanId = " + reportTrashcanId);
            System.out.println(" ========================");

            //조회용 테이블에서 삭제 전 RT 에 있으면 그거 먼저 삭제
            List<Report_Trashcan> duplicatedReportTrashcans = trashcanRepository.findReportTrashcanByIdAndReportCategoryAndTrashcanId(reportCategory,email,reportTrashcanId);
            if(duplicatedReportTrashcans.size() > 0){
                for (Report_Trashcan element : duplicatedReportTrashcans){
                    trashcanRepository.deleteReportTrashcan(element);
                }
            }


            //RT에서 신고내역 삭제 후 조회용 테이블에서 삭제
            trashcanRepository.deleteShowReportTrashcan(reportTrashcanId,reportCategory,email);


        } catch (Exception e){
            logger.error("deleteReportTrashcan - 신고 내역 삭제 실패");
            return ResponseEntity.badRequest().body("6");
        }
        logger.info("deleteReportTrashcan - 신고 내역 삭제 성공");
        return ResponseEntity.ok("7");
    }


    public List<ShowReportTrashcan> myReportTrashcans(String email){
        return trashcanRepository.myReportTrashcans(email);
    }

    @Transactional
    public ResponseEntity<String> autoRegisterTrashcan(@RequestPart(value = "RegisterTrashcanDTO") RegisterTrashcanDTO registerTrashcanDTO, @RequestPart(value = "image") MultipartFile files, String email){
        Long unknownTrashcanId = null;
        try {
            register_unknown(registerTrashcanDTO, files, email);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("1");
        }

        registerTrashcanDTO.sortCoordinates();
        Coordinate coordinate = registerTrashcanDTO.getLatAndLon(registerTrashcanDTO.getCoordinates());
        
        try {
            unknownTrashcanId = findTrashcanIdByLatAndLon(coordinate.getLatitude(), coordinate.getLongitude());
        } catch (Exception e){
            logger.error("findTrashcanIdByLatAndLon 실패");
            return ResponseEntity.badRequest().body("1");
        }
        
        try {
            adminService.acceptNewTrashcan(unknownTrashcanId);
        } catch (Exception e){
            return ResponseEntity.badRequest().body("8");
        }
       try {
           adminService.deleteUnknownTrashcan(unknownTrashcanId);
       } catch (Exception e){
           return ResponseEntity.badRequest().body("11");
       }

        return ResponseEntity.ok("10");
    }
}
