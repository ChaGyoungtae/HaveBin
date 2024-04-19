package com.HaveBinProject.HaveBin.User;

import com.HaveBinProject.HaveBin.RequestDTO.CustomUserDetails;
import com.HaveBinProject.HaveBin.RequestDTO.NicknameDTO;
import com.HaveBinProject.HaveBin.RequestDTO.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<?> responseUserInfo(@RequestBody RegisterDto registerDto){ return userService.join(registerDto); }

//    //이메일 중복 검사 -> EmailController로 이동
//    @PostMapping("/validateDuplicateUser")
//    public ResponseEntity<?> validateDuplicateUser(@RequestBody String email) { return userService.validateDuplicateUser(email); }

    //닉네임 중복 검사
    @PostMapping("/validateDuplicateNickname")
    public ResponseEntity<?> validateDuplicateNickname(@RequestBody NicknameDTO nicknameDTO){
        return userService.validateDuplicateNickname(nicknameDTO);
    }

    @GetMapping("/deleteUser")
    public void deleteUser(@RequestParam("id") Long id){
        userService.deleteUser(id);
    }


    @PostMapping("/getUserdata")
    public ResponseEntity<?> getUserdata(@AuthenticationPrincipal CustomUserDetails userDetails) {return  userService.findOne(userDetails.getUsername()); }


}
