package com.HaveBinProject.HaveBin.DTO;

import jakarta.annotation.Nullable;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    private String email;
    private String password;
    private String nickname;
}
