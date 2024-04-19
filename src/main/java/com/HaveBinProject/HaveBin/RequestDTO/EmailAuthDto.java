package com.HaveBinProject.HaveBin.RequestDTO;

import lombok.Getter;
import lombok.Setter;

//@RedisHash(value = "null", timeToLive = 180)
@Getter @Setter
public class EmailAuthDto {
    private String email;
    private String code;
}
