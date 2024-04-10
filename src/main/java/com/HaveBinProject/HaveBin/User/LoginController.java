package com.HaveBinProject.HaveBin.User;

import com.HaveBinProject.HaveBin.AWS.ImageService;
import com.HaveBinProject.HaveBin.DTO.CustomUserDetails;
import com.HaveBinProject.HaveBin.DTO.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Iterator;

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
//    public ResponseEntity<?> testImgage(MultipartFile image){
//        return ResponseEntity.ok(imageService.uploadImageToS3(image));
//    }

}
