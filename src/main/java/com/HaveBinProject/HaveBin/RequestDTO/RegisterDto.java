package com.HaveBinProject.HaveBin.RequestDTO;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String email;
    private String password;
    private String nickname;
}
