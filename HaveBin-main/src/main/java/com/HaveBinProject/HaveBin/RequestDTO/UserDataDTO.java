package com.HaveBinProject.HaveBin.RequestDTO;

import com.HaveBinProject.HaveBin.User.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDataDTO {
    private String email;
    private String nickname;
    private String profileImgPath;
    private String roleType;

    // 생성자, 게터, 세터 등 필요한 메서드들을 추가할 수 있습니다.

    // 예를 들어 생성자는 다음과 같이 정의할 수 있습니다.
    public UserDataDTO(User user) {
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImgPath = user.getProfile_imgpath();
        this.roleType = user.getRoleType();
    }
}
