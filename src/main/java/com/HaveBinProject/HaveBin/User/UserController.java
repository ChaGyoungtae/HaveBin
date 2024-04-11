package com.HaveBinProject.HaveBin.User;

import com.HaveBinProject.HaveBin.DTO.CustomUserDetails;
import com.HaveBinProject.HaveBin.DTO.RegisterDto;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import retrofit2.Response;

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
    public ResponseEntity<?> responseUserInfo(@RequestBody RegisterDto registerDto){ return userService.join(registerDto); }

    //이메일 중복 검사
    @PostMapping("/validateDuplicateUser")
    public ResponseEntity<?> validateDuplicateUser(@RequestBody String email) { return userService.validateDuplicateUser(email); }

    //닉네임 중복 검사
    @PostMapping("/validateDuplicateNickname")
    public ResponseEntity<?> validateDuplicateNickname(@RequestBody String nickname){ return userService.validateDuplicateNickname(nickname); }

    @GetMapping("/deleteUser")
    public void deleteUser(@RequestParam("id") Long id){
        userService.deleteUser(id);
    }

    @PostMapping("/getUserdata")
    public ResponseEntity<?> getUserdata(@AuthenticationPrincipal CustomUserDetails userDetails) {return  userService.findOne(userDetails.getUsername()); }


}
