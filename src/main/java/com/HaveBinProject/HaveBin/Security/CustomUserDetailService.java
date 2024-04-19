package com.HaveBinProject.HaveBin.Security;

import com.HaveBinProject.HaveBin.RequestDTO.CustomUserDetails;
import com.HaveBinProject.HaveBin.User.User;
import com.HaveBinProject.HaveBin.User.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        List<User> users = userRepository.findByEmail(username);

        System.out.println(users.get(0).getId());
        if (!users.isEmpty()) { return new CustomUserDetails(users.get(0)); }

        return null;
    }

}
