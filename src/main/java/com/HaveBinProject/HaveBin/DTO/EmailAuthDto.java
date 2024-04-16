package com.HaveBinProject.HaveBin.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

//@RedisHash(value = "null", timeToLive = 180)
@Getter @Setter
public class EmailAuthDto {
    private String email;
    private String code;
}
