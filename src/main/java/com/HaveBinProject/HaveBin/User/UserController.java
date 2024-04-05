package com.HaveBinProject.HaveBin.User;

import com.HaveBinProject.HaveBin.DTO.RegisterDto;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    //전체 유저 조회
    @GetMapping("/findMembers")
    public List<User> findAll(){
        return userService.findMembers();
    }

    @PostMapping(value = "/responseUserInfo")
    public Long responseUserInfo(@RequestBody RegisterDto registerDto){
        return userService.join(registerDto);
    }

    //이메일 중복 검사
    @GetMapping("/validateDuplicateUser")
    public String validateDuplicateUser(@RequestParam("email") String email){
        try {
            userService.validateDuplicateUser(email);
        } catch (IllegalStateException e){
            return "중복이메일";
        }
        return "사용가능한 이메일주소입니다";
    }

    //닉네임 중복 검사
    @GetMapping("/validateDuplicateNickname")
    public String validateDuplicateNickname(@RequestParam("nickname") String nickname){
        try{
            userService.validateDuplicateNickname(nickname);
        } catch (IllegalStateException e){
            return "중복닉네임";
        }

        return "사용가능한 닉네임";
    }

    @GetMapping("/deleteUser")
    public void deleteUser(@RequestParam("id") Long id){
        userService.deleteUser(id);
    }
}
