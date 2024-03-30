package com.HaveBinProject.HaveBin.User;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/hello")
    public String test(){
        return "Hello Swagger!";
    }

    //전체 유저 조회
    @GetMapping("/findMembers")
    public List<User> findAll(){
        return userService.findMembers();
    }
}
