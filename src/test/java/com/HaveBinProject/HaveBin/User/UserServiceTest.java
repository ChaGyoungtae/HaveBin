package com.HaveBinProject.HaveBin.User;

import com.HaveBinProject.HaveBin.DTO.RegisterDto;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void 회원_가입() throws Exception {
        //given
        RegisterDto registerDto = new RegisterDto();

        registerDto.setEmail("test_email");
        registerDto.setNickname("test_nickname");
        registerDto.setPassword("test_password");

        //when
        userService.join(registerDto);

        }
}