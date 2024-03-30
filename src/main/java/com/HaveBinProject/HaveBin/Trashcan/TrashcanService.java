package com.HaveBinProject.HaveBin.Trashcan;

import com.HaveBinProject.HaveBin.DTO.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrashcanService {

    private final TrashcanRepository trashcanRepository;

    @Transactional
    //새로운 쓰레기통 등록 아직 미구현
    public Long register(Trashcan trashcan){
        return trashcanRepository.saveOne(trashcan);
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
}
