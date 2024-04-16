package com.HaveBinProject.HaveBin.MailAuth;

import com.HaveBinProject.HaveBin.DTO.EmailAuthDto;
import com.HaveBinProject.HaveBin.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmailController {

    // 이메일 인증
    private final MailService mailService;

    private final EmailAuthService emailAuthService;

    private final UserService userService;

    @PostMapping("/validateDuplicateUser")
    public ResponseEntity<?> mailConfirm(@RequestBody String email) throws Exception {
        boolean pass = userService.validateDuplicateUser(email);

        if(!pass) { return ResponseEntity.badRequest().body("duplicate"); }

        String code = mailService.sendSimpleMessage(email);
        System.out.println("인증코드 : " + code);

        emailAuthService.saveDataWithExpiration(email, code, 300);

        return ResponseEntity.ok("success");
    }

    @PostMapping("/mailAuth")
    public ResponseEntity<?> mailAuth(@RequestBody EmailAuthDto dto) throws Exception {

        try {
            if(emailAuthService.getData(dto.getEmail()).equals(dto.getCode())) {
                return ResponseEntity.ok("success!");
            } else {
                return ResponseEntity.badRequest().body("false");
            }
        } catch (NullPointerException e) {
            System.out.println("인증 코드 시간이 만료되었습니다.");
            return ResponseEntity.badRequest().body("timeout");
        }
    }
}