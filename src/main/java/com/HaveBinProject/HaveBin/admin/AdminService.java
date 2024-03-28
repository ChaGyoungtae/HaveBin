package com.HaveBinProject.HaveBin.admin;

import com.HaveBinProject.HaveBin.Trashcan.TrashcanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService {

    private final TrashcanRepository trashcanRepository;

    public void saveAll(){

    }




}
