package com.HaveBinProject.HaveBin.User;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    //회원가입
    public Long join(RegisterDto registerDto){
        User user = new User();

        user.setEmail(registerDto.getEmail());
        user.setNickname(registerDto.getEmail());
        user.setKakaoid(registerDto.getKakaoId());
        user.setPassword(registerDto.getPassword());
        user.setProfile_imgpath(registerDto.getProfile_imgpath());
        //중복 회원 검출
        validateDuplicateUser(user);

        return userRepository.save(user);
    }

    @Transactional
    public void validateDuplicateUser(User user) {

        //중복 검출 시 예외 발생
        List<User> findEmails = userRepository.findByEmail(user.getEmail());
        if(!findEmails.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    //회원 전체 조회, 읽기 전용표시로 성능향상
    public List<User> findMembers() {

        return userRepository.findAll();
    }

    //단건 조회
    public User findOne(Long id){
        return userRepository.find(id);
    }
}
