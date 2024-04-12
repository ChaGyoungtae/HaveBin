package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.DTO.CustomUserDetails;
import com.HaveBinProject.HaveBin.DTO.RegisterTrashcanDTO;
import com.HaveBinProject.HaveBin.DTO.ReportTrashcanDTO;
import com.HaveBinProject.HaveBin.DTO.ResponseDTO;
import com.HaveBinProject.HaveBin.User.User;
import com.HaveBinProject.HaveBin.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrashcanService {

    private final TrashcanRepository trashcanRepository;
    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());



    @Transactional
    //새로운 쓰레기통 등록 아직 미구현
    public ResponseEntity<?> register_unknown(RegisterTrashcanDTO registerTrashcanDTO){

        try{
            Unknown_Trashcan unknown_trashcan = new Unknown_Trashcan();
            unknown_trashcan.setLatitude(registerTrashcanDTO.getLatitude());
            unknown_trashcan.setLongitude(registerTrashcanDTO.getLongitude());
            unknown_trashcan.setRoadviewImgpath(registerTrashcanDTO.getRoadviewImgpath());
            unknown_trashcan.setCategories(registerTrashcanDTO.getCategories());
            unknown_trashcan.setUserId(registerTrashcanDTO.getUserId());
            unknown_trashcan.setState(registerTrashcanDTO.getState());
            unknown_trashcan.setDate(registerTrashcanDTO.getReport_date());
            Long trashcanId = trashcanRepository.saveOne_unknown(unknown_trashcan);

            if(unknown_trashcan.getUnknown_trashcan_id() != trashcanId){
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
            User user = userRepository.findByEmail(email).get(0);
            Long trashcanId = Long.parseLong(reportTrashcanDTO.getTrashcanId());
            Report_Trashcan reportTrashcan = new Report_Trashcan();
            Trashcan trashcan = trashcanRepository.find(trashcanId);
            reportTrashcan.setTrashcan(trashcan);
            reportTrashcan.setUser(user);
            reportTrashcan.setReport_category(reportTrashcanDTO.getReport_category());
            reportTrashcan.setModifyStatus(false);
            Long reportTrashcanId = trashcanRepository.saveReportTrashcan(reportTrashcan);

            if(trashcanRepository.findReportTrashcan(reportTrashcanId) != reportTrashcan){
                throw new IllegalStateException("신고 실패");
            }
        } catch (IllegalStateException e) {
            logger.error("Report failed");
            return ResponseEntity.badRequest().body("Report failed");
        }
        return ResponseEntity.ok("Report success!");

    }

    public int findReportCount(Long TrashcanId){
        return trashcanRepository.findReportCount(TrashcanId);
    }
}
