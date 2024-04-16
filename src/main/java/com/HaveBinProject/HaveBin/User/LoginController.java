package com.HaveBinProject.HaveBin.User;

import com.HaveBinProject.HaveBin.AWS.ImageService;
import com.HaveBinProject.HaveBin.DTO.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final ImageService imageService;

    @PostMapping(value = "/testlogin")
    public ResponseEntity<?> testlogin(@AuthenticationPrincipal CustomUserDetails userDetails){

        String email = userDetails.getUsername();
        Long userid = userDetails.getUserid();
        System.out.println("test중...:" + userid);

        return ResponseEntity.ok(userService.findId(email));
    }

//    @PostMapping(value = "/testImgage")
//    public ResponseEntity<?> testImgage(String test, @RequestPart(value = "image")MultipartFile files){
//        return ResponseEntity.ok(imageService.uploadImageToS3(files, "User"));
//    }

}
