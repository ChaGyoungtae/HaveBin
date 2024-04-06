package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.DTO.RegisterTrashcanDTO;
import com.HaveBinProject.HaveBin.DTO.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            Long trashcanId = trashcanRepository.saveOne_unknown(unknown_trashcan);

            if(unknown_trashcan.getTrashcan_id() != trashcanId){
                throw new IllegalStateException("register_unknown_exception");
            }
        } catch (IllegalStateException e){
            logger.error("register_unknown_error");
            return ResponseEntity.badRequest().body("register_unknown_error");
        }
        logger.info("register Success");
        return ResponseEntity.ok("register_success");


    }

//    @Transactional
//    public ResponseEntity<?> register(Unknown_Trashcan unknown_trashcan){
//    }

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
}
