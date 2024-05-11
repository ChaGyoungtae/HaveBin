package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.RequestDTO.*;
import com.HaveBinProject.HaveBin.AWS.ImageService;
import com.HaveBinProject.HaveBin.RequestDTO.RegisterTrashcanDTO;
import com.HaveBinProject.HaveBin.RequestDTO.ReportTrashcanDTO;
import com.HaveBinProject.HaveBin.RequestDTO.PosResponse;
import com.HaveBinProject.HaveBin.ResponseDTO.TrashcanData;
import com.HaveBinProject.HaveBin.User.User;
import com.HaveBinProject.HaveBin.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrashcanService {

    private final TrashcanRepository trashcanRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;


    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Transactional
    //새로운 쓰레기통 등록 아직 미구현
    // 5미터 반경에 쓰레기통이 이미 있으면 등록 취소
    public ResponseEntity<?> register_unknown(RegisterTrashcanDTO registerTrashcanDTO, MultipartFile file, String email){

        //Trashcan, UnknownTrashcan 두 테이블 모두 없는 신규 쓰레기통이면 등록
        try{

            Double lat = registerTrashcanDTO.getLatitude();
            Double lon = registerTrashcanDTO.getLongitude();

            //5미터는 0.0000449
            Double interval = 0.0000449;
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

            if(unknownTrashcanList.size() >0){
                throw new IllegalStateException("이미 등록된 쓰레기통 입니다.");
            }

            Long userId = userRepository.findIdByEmail(email);
            Unknown_Trashcan unknown_trashcan = registerTrashcanDTO.toEntity(registerTrashcanDTO, userId);

            unknown_trashcan.setRoadviewImgpath(imageService.uploadImageToS3(file,"Unknown_Trashcan"));

            Long trashcanId = trashcanRepository.saveOne_unknown(unknown_trashcan);

        } catch (IllegalStateException e){
            logger.error("register_unknown_error");

            return ResponseEntity.badRequest().body("이미 등록되거나 신고되어있는 쓰레기통입니다.");
        }
        logger.info("register Success");
        return ResponseEntity.ok("register_success");
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

            List<String> findReportTrashcanEmail = trashcanRepository.findReportTrashcanByIdAndReportCategoryAndTrashcanId(reportCategory, email, reportTrashcanId);

            if (findReportTrashcanEmail.isEmpty() == false) {
                throw new IllegalStateException("이미 신고한 쓰레기통입니다.");
            }

            User user = userRepository.findByEmail(email).get(0);
            Long trashcanId = Long.parseLong(reportTrashcanDTO.getTrashcanId());
            Trashcan trashcan = trashcanRepository.find(trashcanId);
            //카테고리 / 0 : 부정확한 위치, 1:쓰레기통 없음
            Report_Trashcan reportTrashcan = new Report_Trashcan(user,trashcan,reportTrashcanDTO.getReportCategory());
            trashcanRepository.saveReportTrashcan(reportTrashcan);


            //조회용 테이블에도 저장
            ShowReportTrashcan showReportTrashcan = new ShowReportTrashcan(email,trashcanId,reportCategory);
            trashcanRepository.saveShowReportTrashcan(showReportTrashcan);

        } catch (IllegalStateException e) {
            logger.error("Report failed");
            return ResponseEntity.badRequest().body("Report failed");
        }

        return ResponseEntity.ok("Report success!");

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
            trashcanRepository.deleteReportTrashcan(reportTrashcanId, reportCategory, email);

            //RT에서 신고내역 삭제 후 조회용 테이블에서 삭제
            trashcanRepository.deleteShowReportTrashcan(reportTrashcanId,reportCategory,email);


        } catch (Exception e){
            return ResponseEntity.badRequest().body("삭제 실패");
        }
        return ResponseEntity.ok("삭제완료");
    }


    public List<MyReportTrashcanDTO> myReportTrashcans(String email){
        return trashcanRepository.myReportTrashcans(email);
    }
}
