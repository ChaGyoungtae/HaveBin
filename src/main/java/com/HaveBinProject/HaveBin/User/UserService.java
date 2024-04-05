package com.HaveBinProject.HaveBin.User;

import com.HaveBinProject.HaveBin.DTO.RegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    //일반회원가입
    public Long join(RegisterDto registerDto){
        User user = new User();

        user.setEmail(registerDto.getEmail());
        user.setNickname(registerDto.getNickname());
        user.setPassword(registerDto.getPassword());

        return userRepository.save(user);
    }

    @Transactional
    public void validateDuplicateUser(String email) {

        //중복 검출 시 예외 발생
        List<User> findEmails = userRepository.findByEmail(email);
        if(!findEmails.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    @Transactional
    public void validateDuplicateNickname(String nickname){
        //이메일 중복 검출 시 예외 발생
        List<User> findNicknames = userRepository.findByNickname(nickname);
        if(!findNicknames.isEmpty()){
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
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

    //유저 삭제
    @Transactional
    public void deleteUser(Long id){
        userRepository.delete(id);
    }
}
