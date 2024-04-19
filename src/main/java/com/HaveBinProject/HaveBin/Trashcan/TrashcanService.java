package com.HaveBinProject.HaveBin.Trashcan;


import com.HaveBinProject.HaveBin.RequestDTO.*;
import com.HaveBinProject.HaveBin.AWS.ImageService;
import com.HaveBinProject.HaveBin.RequestDTO.RegisterTrashcanDTO;
import com.HaveBinProject.HaveBin.RequestDTO.ReportTrashcanDTO;
import com.HaveBinProject.HaveBin.RequestDTO.ResponseDTO;
import com.HaveBinProject.HaveBin.User.User;
import com.HaveBinProject.HaveBin.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
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
    public ResponseEntity<?> register_unknown(RegisterTrashcanDTO registerTrashcanDTO, MultipartFile file, String email){

        try{
            Long userId = userRepository.findIdByEmail(email);
            Unknown_Trashcan unknown_trashcan = registerTrashcanDTO.toEntity(registerTrashcanDTO, userId);

            unknown_trashcan.setRoadviewImgpath(imageService.uploadImageToS3(file,"Unknown_Trashcan"));

            Long trashcanId = trashcanRepository.saveOne_unknown(unknown_trashcan);

            if(!unknown_trashcan.getUnknown_trashcan_id().equals(trashcanId)){
                throw new IllegalStateException("register_unknown_exception");
            }
        } catch (IllegalStateException e){
            logger.error("register_unknown_error");
            return ResponseEntity.badRequest().body("register_unknown_error");
        }
        logger.info("register Success");
        return ResponseEntity.ok("register_success");


    }

    //전체 쓰레기통 조회
    public List<Trashcan> findTrashcans(){
        return trashcanRepository.findAll();
    }
    //특정 쓰레기통 조회
    public Trashcan findOnd(Long id){
        return trashcanRepository.find(id);
    }

    public List<Trashcan> findNearTrashcans(ResponseDTO responseDTO){
        return trashcanRepository.findNear(responseDTO);
    }

    //쓰레기통 신고
    @Transactional
    public ResponseEntity<?> reportTrashcan(ReportTrashcanDTO reportTrashcanDTO, String email){

        try{
            String reportCategory = reportTrashcanDTO.getReport_category();
            Long reportTrashcanId = Long.parseLong(reportTrashcanDTO.getTrashcanId());
            List<String> findReportTrashcanEmail = trashcanRepository.findReportTrashcanByIdAndReportCategoryAndTrashcanId(reportCategory, email, reportTrashcanId);

            if (findReportTrashcanEmail.isEmpty() == false) {
                throw new IllegalStateException("이미 신고한 쓰레기통입니다.");
            }

            User user = userRepository.findByEmail(email).get(0);
            Long trashcanId = Long.parseLong(reportTrashcanDTO.getTrashcanId());
            Trashcan trashcan = trashcanRepository.find(trashcanId);
            Report_Trashcan reportTrashcan = new Report_Trashcan(user,trashcan,reportTrashcanDTO.getReport_category());
            trashcanRepository.saveReportTrashcan(reportTrashcan);

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
    public ResponseEntity<?> deleteReportTrashcan(ReportTrashcanDTO deleteReportTrashcanDTO){
        try{
            Long reportTrashcanId = Long.parseLong(deleteReportTrashcanDTO.getTrashcanId());
            String reportCategory = deleteReportTrashcanDTO.getReport_category();

            System.out.println(" ========================");
            System.out.println("reportCategory = " + reportCategory);
            System.out.println("reportTrashcanId = " + reportTrashcanId);
            System.out.println(" ========================");

            trashcanRepository.deleteReportTrashcan(reportTrashcanId, reportCategory);

        } catch (Exception e){
            return ResponseEntity.badRequest().body("삭제 실패");
        }
        return ResponseEntity.ok("삭제완료");
    }

    @Transactional
    public List<SendReportTrashcanDTO> findReportTrashcans(String email){
        return trashcanRepository.findReportTrashcansByEmail(email);
    }
}
