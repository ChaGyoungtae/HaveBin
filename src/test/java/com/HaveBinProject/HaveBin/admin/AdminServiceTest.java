package com.HaveBinProject.HaveBin.admin;

import com.HaveBinProject.HaveBin.Trashcan.TrashcanRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AdminServiceTest {

    @Autowired AdminService adminService;
    @Autowired
    TrashcanRepository trashcanRepository;

    @Test
    public void 쓰레기통정보_전체_저장() throws Exception {
        //given

        //when

        //then
        }
}