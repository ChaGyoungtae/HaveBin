package com.HaveBinProject.HaveBin.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String email;
    private String password;
    private String nickname;
    private Long kakaoId;
    private String profile_imgpath;
}
