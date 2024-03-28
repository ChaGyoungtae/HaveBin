package com.HaveBinProject.HaveBin.User;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired UserService userService;

    @Autowired UserRepository userRepository;

    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception {
        //given
        User user = new User();
        user.setEmail("admin");
        user.setNickname("admin");
        user.setPassword("1234");
        user.setKakaoid(1L);
        user.setProfile_imgpath("img_path");


        //when
        Long saveId = userService.join(user);

        //then
        assertThat(saveId).isEqualTo(user.getId());
        }
        
    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        
        //when
        
        //then
        }    
}